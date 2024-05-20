package com.sluv.server.domain.auth.service;

import static com.sluv.server.domain.auth.enums.SnsType.APPLE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sluv.server.domain.auth.dto.AuthRequestDto;
import com.sluv.server.domain.auth.dto.SocialUserInfoDto;
import com.sluv.server.domain.closet.service.ClosetService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.enums.UserAge;
import com.sluv.server.domain.user.enums.UserGender;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.jwt.exception.ExpiredTokenException;
import com.sluv.server.global.jwt.exception.InvalidateTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AppleUserService {
    private final UserRepository userRepository;
    private final ClosetService closetService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${apple.clientId}")
    private String clientId;

    @Value("${apple.openKey}")
    private String appleOpenKeys;

    @Value("${apple.iss}")
    private String issUrl;

    public User appleLogin(AuthRequestDto request) throws Exception {
        String identityToken = request.getAccessToken();

        // 1. 검증
        if (!verifyIdToken(identityToken)) {
            throw new InvalidateTokenException();
        }

        // 2. UserIngoDto 생성
        SocialUserInfoDto userInfo = getAppleUserInfo(identityToken);

        // 3. idToken의 정보로 DB 탐색 및 등록
        return registerAppleUserIfNeed(userInfo);
    }

    /**
     * == identitiyToken이 유효한 토큰인지 확인 ==
     *
     * @param identityToken
     * @return 유효 여부
     * @throws Exception
     */
    private boolean verifyIdToken(String identityToken) throws Exception {
        String[] pieces = identityToken.split("\\.");
        if (pieces.length != 3) {
            return false;
        }
        String header = new String(Base64.getUrlDecoder().decode(pieces[0]));
        String payload = new String(Base64.getUrlDecoder().decode(pieces[1]));

        JsonNode headerNode = objectMapper.readTree(header);
        JsonNode payloadNode = objectMapper.readTree(payload);

        String algorithm = headerNode.get("alg").asText();

        String idKid = headerNode.get("kid").asText();

        if (!algorithm.equals("RS256")) {
            return false;
        }
        // 원래 처리해야하는데 왜 우리 토큰엔 없죠...? - JunKer
//        String nonce = payloadNode.get("nonce").asText();
//        if (!nonce.equals(this.nonce)) {
//            return false;
//        }
        String iss = payloadNode.get("iss").asText();
        if (!iss.equals(issUrl)) {
            return false;
        }

        String aud = payloadNode.get("aud").asText();
        if (!aud.equals(this.clientId)) {
            return false;
        }

        long exp = payloadNode.get("exp").asLong();
        if (exp < System.currentTimeMillis() / 1000) {
            throw new ExpiredTokenException();
        }

        if (getPublicKeyFromPEM(identityToken, idKid) == null) {
            return false;
        }

        return true;

    }

    /**
     * == idToken이 검증된 토큰인지 확인 ==
     *
     * @param identityToken
     * @param identityKid
     * @return
     * @throws Exception
     */
    public Claims getPublicKeyFromPEM(String identityToken, String identityKid) throws Exception {
        JsonNode correctKey = getApplePublicKey(identityKid);
        String tN = correctKey.get("n").asText();
        String tE = correctKey.get("e").asText();
        String kty = correctKey.get("kty").asText();

        byte[] nBytes = Base64.getUrlDecoder().decode(tN);
        byte[] eBytes = Base64.getUrlDecoder().decode(tE);

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
        KeyFactory keyFactory = KeyFactory.getInstance(kty);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build();

        return parser.parseClaimsJws(identityToken).getBody();
    }

    /**
     * == Apple에게 공개키를 요청 ==
     *
     * @param identityKid
     * @return 알맞는 공개키의 JsonNode
     * @throws Exception
     */

    private JsonNode getApplePublicKey(String identityKid) throws Exception {
        URL url = new URL(appleOpenKeys);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        JsonNode jsonNode = objectMapper.readTree(connection.getInputStream());
        JsonNode keysNode = jsonNode.get("keys");

        JsonNode correctKey = null;

        for (JsonNode keyNode : keysNode) {
            String kid = keyNode.get("kid").asText();
            if (kid.equals(identityKid)) {
                correctKey = keyNode;
                break;
            }
        }

        return correctKey;

    }

    /**
     * identityToken의 정보 SocialUserDto로 변환
     *
     * @param identityToken
     * @return SocialUserInfoDto
     * @throws JsonProcessingException
     */

    private SocialUserInfoDto getAppleUserInfo(String identityToken) throws JsonProcessingException {
        String[] pieces = identityToken.split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(pieces[1]));

        JsonNode jsonNode = objectMapper.readTree(payload);

        String email = jsonNode.get("email").asText();

        String profileImgUrl;
        try {
            profileImgUrl = jsonNode.get("picture").asText();
        } catch (Exception e) {
            profileImgUrl = null;
        }

        String gender;

        try {
            gender = jsonNode.get("gender").asText();
        } catch (Exception e) {
            gender = null;
        }

        String ageRange;
        try {
            ageRange = jsonNode.get("birthdate").asText();
        } catch (Exception e) {
            ageRange = null;
        }

        return SocialUserInfoDto.builder()
                .email(email)
                .profileImgUrl(profileImgUrl)
                .gender(convertGender(gender))
                .ageRange(convertAge(ageRange))
                .build();
    }

    /**
     * == identityToken을 기반으로 user 등록 및 조회 ==
     *
     * @param userInfoDto
     * @return User
     */

    private User registerAppleUserIfNeed(SocialUserInfoDto userInfoDto) {
        User user = userRepository.findByEmail(userInfoDto.getEmail()).orElse(null);

        if (user == null) {
            userRepository.save(
                    User.toEntity(userInfoDto, APPLE)
            );

            user = userRepository.findByEmail(userInfoDto.getEmail())
                    .orElseThrow(UserNotFoundException::new);

            // 생성과 동시에 기본 Closet 생성
            closetService.postBasicCloset(user);
        }
        return user;
    }

    private UserGender convertGender(String gender) {
        UserGender userGender = UserGender.UNKNOWN;
        if (gender != null) {
            if (gender.equals("male")) {
                userGender = UserGender.MALE;
            }
            if (gender.equals("female")) {
                userGender = UserGender.FEMALE;
            }
        }
        return userGender;
    }

    private UserAge convertAge(String age) {
        UserAge userGender = UserAge.UNKNOWN;
        if (age != null) {
            int startAge = Integer.parseInt(age.split("-")[0]);
            for (UserAge value : UserAge.values()) {
                if (startAge == value.getStartAge()) {
                    userGender = value;
                    break;
                }
            }
        }
        return userGender;
    }
}
