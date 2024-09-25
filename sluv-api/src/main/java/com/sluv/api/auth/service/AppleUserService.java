package com.sluv.api.auth.service;

import static com.sluv.domain.auth.enums.SnsType.APPLE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sluv.api.auth.dto.request.AuthRequest;
import com.sluv.common.jwt.exception.InvalidateTokenException;
import com.sluv.domain.auth.dto.SocialUserInfoDto;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.enums.UserAge;
import com.sluv.domain.user.enums.UserGender;
import com.sluv.infra.oauth.apple.ApplePlatformClient;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AppleUserService {

    private final AuthService authService;
    private final ApplePlatformClient applePlatformClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public User appleLogin(AuthRequest request) throws Exception {
        String identityToken = request.getAccessToken();

        // 1. 검증
        if (!applePlatformClient.verifyIdToken(identityToken)) {
            throw new InvalidateTokenException();
        }

        // 2. UserIngoDto 생성
        SocialUserInfoDto userInfo = getAppleUserInfo(identityToken);

        // 3. idToken의 정보로 DB 탐색 및 등록
        return authService.getOrCreateUser(userInfo, APPLE, request.getFcm());
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
