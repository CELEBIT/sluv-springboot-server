package com.sluv.server.domain.auth.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sluv.server.domain.auth.dto.AuthRequestDto;

import com.sluv.server.domain.auth.dto.AuthResponseDto;
import com.sluv.server.domain.auth.dto.SocialUserInfoDto;
import com.sluv.server.domain.user.dto.UserDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.NotFoundUserException;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.jwt.JwtProvider;

import com.sluv.server.global.jwt.exception.InvalidateTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;

import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import static com.sluv.server.domain.auth.enums.SnsType.APPLE;


@Service
@RequiredArgsConstructor
public class AppleUserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${apple.clientId}")
    private String clientId;

    @Value("${apple.openKeys}")
    private String appleOpenKeys;

    @Value("${apple.iss}")
    private String issUrl;

    public AuthResponseDto appleLogin(AuthRequestDto request) throws Exception {
        String idToken = request.getAccessToken();

        // 1. 검증
        if(!verifyIdToken(idToken)){
            throw new InvalidateTokenException();
        }

        // 2. UserIngoDto 생성
        SocialUserInfoDto userInfo = getAppleUserInfo(idToken);

        // 3. idToken의 정보로 DB 탐색 및 등록
        User appleUser = registerAppleUserIfNeed(userInfo);

        // 2. userToken 생성
        return AuthResponseDto.builder()
                .token(createUserToken(appleUser))
                .build();
    }

    /**
     * == idToken이 유효한 토큰인지 확인 ==
     *
     * @param idToken
     * @return
     * @throws Exception
     */
    private boolean verifyIdToken(String idToken) throws Exception {
        String[] pieces = idToken.split("\\.");
        if (pieces.length != 3) {
            System.out.println("길이");
            return false;
        }
        String header = new String(Base64.getUrlDecoder().decode(pieces[0]));
        String payload = new String(Base64.getUrlDecoder().decode(pieces[1]));

        JsonNode headerNode = objectMapper.readTree(header);
        JsonNode payloadNode = objectMapper.readTree(payload);

        String algorithm = headerNode.get("alg").asText();

        String idKid = headerNode.get("kid").asText();

        if (!algorithm.equals("RS256")) {
            System.out.println("알고리즘");
            return false;
        }
//        String nonce = payloadNode.get("nonce").asText();
//        if (!nonce.equals(this.nonce)) {
//            return false;
//        }
        String iss = payloadNode.get("iss").asText();
        if (!iss.equals(issUrl)) {
            System.out.println("appleId");
            return false;
        }
        String aud = payloadNode.get("aud").asText();
        if (!aud.equals(this.clientId)) {
            System.out.println("id");
            return false;
        }
        long exp = payloadNode.get("exp").asLong();
        if (exp < System.currentTimeMillis() / 1000) {
            System.out.println("만료");
            return false;
        }

        if(getPublicKeyFromPEM(idToken, idKid) == null){
            return false;
        }

        return true;

    }

    /**
     * == idToken이 검증된 토큰인지 확인 ==
     *
     * @param idToken
     * @param idKid
     * @return
     * @throws Exception
     */
    public Claims getPublicKeyFromPEM(String idToken, String idKid) throws Exception {
        JsonNode correctKey = getApplePublicKey(idKid);
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

        return parser.parseClaimsJws(idToken).getBody();
    }

    /**
     * == Apple에게 공개키를 요청 ==
     *
     * @param idKid
     * @return 알맞는 공개키의 JsonNode
     * @throws Exception
     */

    private JsonNode getApplePublicKey(String idKid) throws Exception {
        URL url = new URL(appleOpenKeys);


        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        JsonNode jsonNode = objectMapper.readTree(connection.getInputStream());
        JsonNode keysNode = jsonNode.get("keys");

        JsonNode correctKey = null;

        for (JsonNode keyNode : keysNode) {
            String kid = keyNode.get("kid").asText();
            if (kid.equals(idKid)) {
                correctKey = keyNode;
                break;
            }
        }

        return correctKey;

    }

    /**
     * == user 정보를 기반으로 user Access Token 생성 ==
     *
     * @param user
     * @return user Access Token
     */
    private String createUserToken(User user) {

        return jwtProvider.createAccessToken(UserDto.builder().id(user.getId()).build());
    }

    /**
     * idToken의 정보 SocialUserDto로 변환
     *
     * @param idToken
     * @return
     * @throws JsonProcessingException
     */

    private SocialUserInfoDto getAppleUserInfo(String idToken) throws JsonProcessingException {
        String[] pieces = idToken.split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(pieces[1]));

        JsonNode jsonNode = objectMapper.readTree(payload);

        String email = jsonNode.get("email").asText();

        String profileImgUrl;
        try{
            profileImgUrl = jsonNode.get("picture").asText();
        }catch (Exception e){
            profileImgUrl = null;
        }

        String gender;

        try{
            gender = jsonNode.get("gender").asText();
        }catch (Exception e){
            gender = null;
        }

        String ageRange;
        try{
            ageRange = jsonNode.get("age_range").asText();
        }catch (Exception e){
            ageRange = null;
        }

        return SocialUserInfoDto.builder()
                .email(email)
                .profileImgUrl(profileImgUrl)
                .gender(gender)
                .ageRange(ageRange)
                .build();
    }

    /**
     * == idToken을 기반으로 user 등록 및 조회 ==
     *
     * @param userInfoDto
     * @return
     */

    private User registerAppleUserIfNeed(SocialUserInfoDto userInfoDto) {
        User user = userRepository.findByEmail(userInfoDto.getEmail()).orElse(null);

        if(user == null) {
            userRepository.save(User.builder()
                    .email(userInfoDto.getEmail())
                    .snsType(APPLE)
                    .profileImgUrl(userInfoDto.getProfileImgUrl())
                    .ageRange(userInfoDto.getAgeRange())
                    .gender(userInfoDto.getGender())
                    .build());

            user = userRepository.findByEmail(userInfoDto.getEmail())
                    .orElseThrow(NotFoundUserException::new);
        }
        return user;
    }
}
