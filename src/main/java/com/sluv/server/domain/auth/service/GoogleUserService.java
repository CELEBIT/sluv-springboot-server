package com.sluv.server.domain.auth.service;

import static com.sluv.server.domain.auth.enums.SnsType.GOOGLE;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.sluv.server.domain.auth.dto.AuthRequestDto;
import com.sluv.server.domain.auth.dto.SocialUserInfoDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.enums.UserAge;
import com.sluv.server.domain.user.enums.UserGender;
import com.sluv.server.global.jwt.exception.InvalidateTokenException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class GoogleUserService {

    private final AuthService authService;


    @Value("${spring.security.oauth2.client.android}")
    private String CLIENT_ANDROID;

    @Value("${spring.security.oauth2.client.apple}")
    private String CLIENT_APPLE;

    @Transactional
    public User googleLogin(AuthRequestDto request) {
        String idToken = request.getAccessToken();

        // 1. idToken 검증
        SocialUserInfoDto verifiedIdToken = verifyIdToken(idToken);

        // 2. user 정보로 DB 탐색 및 등록
        return authService.getOrCreateUser(verifiedIdToken, GOOGLE, request.getFcm());
    }

    /**
     * == 프론트에서 준 idToken의 유효성 검사
     *
     * @param idToken
     * @return SocialUserInfoDto
     * @throws InvalidateTokenException
     */
    private SocialUserInfoDto verifyIdToken(String idToken) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance()).setAudience(Arrays.asList(CLIENT_ANDROID, CLIENT_APPLE)).build();

        try {
            GoogleIdToken verifiedIdToken = verifier.verify(idToken);

            return convertResponseToSocialUserInfoDto(verifiedIdToken);
        } catch (Exception e) {
            throw new InvalidateTokenException();
        }

    }

    /**
     * == GoogleIdToken을 SocialUserInfoDto로 변경
     *
     * @param idToken
     * @return SocialUserInfoDto
     */

    private static SocialUserInfoDto convertResponseToSocialUserInfoDto(GoogleIdToken idToken) {

        // responseBody에 있는 정보를 꺼냄
        String email = idToken.getPayload().getEmail();
        String profileImgUrl = (String) idToken.getPayload().get("picture");

        //Google에서 성별과 연령대 정보를 제공하지 않는 것 같음 23.3.5 -junker-

        return SocialUserInfoDto.builder().email(email).profileImgUrl(profileImgUrl)
                .gender(UserGender.UNKNOWN)
                .ageRange(UserAge.UNKNOWN)
                .build();
    }

}
