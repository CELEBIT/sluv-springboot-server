package com.sluv.api.auth.service;

import static com.sluv.domain.auth.enums.SnsType.GOOGLE;

import com.sluv.api.auth.dto.request.AuthRequest;
import com.sluv.domain.auth.dto.SocialUserInfoDto;
import com.sluv.domain.user.entity.User;
import com.sluv.infra.oauth.google.GooglePlatformClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class GoogleUserService {

    private final AuthService authService;
    private final GooglePlatformClient googlePlatformClient;

    @Transactional
    public User googleLogin(AuthRequest request) {
        String idToken = request.getAccessToken();

        // 1. idToken 검증
        SocialUserInfoDto verifiedIdToken = googlePlatformClient.verifyIdToken(idToken);

        // 2. user 정보로 DB 탐색 및 등록
        return authService.getOrCreateUser(verifiedIdToken, GOOGLE, request.getFcm());
    }


}
