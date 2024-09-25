package com.sluv.api.auth.service;

import static com.sluv.domain.auth.enums.SnsType.KAKAO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sluv.api.auth.dto.request.AuthRequest;
import com.sluv.domain.auth.dto.SocialUserInfoDto;
import com.sluv.domain.user.entity.User;
import com.sluv.infra.oauth.kakao.KakaoPlatformClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class KakaoUserService {

    private final AuthService authService;
    private final KakaoPlatformClient kakaoPlatformClient;

    public User kakaoLogin(AuthRequest request) throws JsonProcessingException {
        String accessToken = request.getAccessToken();
        // 1. accessToken으로 user 정보 요청
        SocialUserInfoDto userInfo = kakaoPlatformClient.getKakaoUserInfo(accessToken);

        // 2. user 정보로 DB 탐색 및 등록
        return authService.getOrCreateUser(userInfo, KAKAO, request.getFcm());
    }

}
