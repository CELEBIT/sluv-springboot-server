package com.sluv.api.auth.controller;

import com.sluv.api.auth.dto.request.AuthRequest;
import com.sluv.api.auth.dto.request.AutoLoginRequest;
import com.sluv.api.auth.dto.response.AuthResponse;
import com.sluv.api.auth.dto.response.AutoLoginResponse;
import com.sluv.api.auth.service.AppleUserService;
import com.sluv.api.auth.service.AuthService;
import com.sluv.api.auth.service.GoogleUserService;
import com.sluv.api.auth.service.KakaoUserService;
import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.common.response.SuccessResponse;
import com.sluv.common.annotation.CurrentUserId;
import com.sluv.domain.auth.enums.SnsType;
import com.sluv.domain.user.entity.User;
import com.sluv.infra.counter.visit.VisitCounter;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KakaoUserService kakaoUserService;
    private final GoogleUserService googleUserService;
    private final AppleUserService appleUserService;
    private final AuthService authService;
    private final VisitCounter visitCounter;


    @Operation(summary = "소셜 로그인", description = "KAKAO:[AccessToken], GOOGLE, APPLE: [IdToken]")
    @PostMapping("/social-login")
    public ResponseEntity<SuccessDataResponse<AuthResponse>> socialLogin(@RequestBody AuthRequest request)
            throws Exception {
        User loginUser = null;

        SnsType userSnsType = SnsType.fromString(request.getSnsType());
        switch (userSnsType) {
            case KAKAO -> loginUser = kakaoUserService.kakaoLogin(request);
            case GOOGLE -> loginUser = googleUserService.googleLogin(request);
            case APPLE -> loginUser = appleUserService.appleLogin(request);
        }

        visitCounter.countVisit(loginUser.getId());

        AuthResponse response = authService.getAuthResDto(loginUser);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Deprecated
    @Operation(summary = "*자동 로그인", description = "토큰 만료 시 error code : 4002")
    @GetMapping("/auto-login")
    public ResponseEntity<SuccessDataResponse<AutoLoginResponse>> autoLogin(@CurrentUserId Long userId) {
        visitCounter.countVisit(userId);
        User user = authService.findLogInUser(userId);
//        authService.checkFcm(user);
        AutoLoginResponse response = AutoLoginResponse.of(user);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "*자동 로그인", description = "토큰 만료 시 error code : 4002")
    @PostMapping("/auto-login")
    public ResponseEntity<SuccessDataResponse<AutoLoginResponse>> autoLoginWithFcm(@CurrentUserId Long userId,
                                                                                   @Nullable @RequestBody AutoLoginRequest request) {
        visitCounter.countVisit(userId);
        if (request != null) {
            authService.changeFcm(userId, request.getFcm());
        }
        User user = authService.findLogInUser(userId);
//        authService.checkFcm(user);
        AutoLoginResponse response = AutoLoginResponse.of(user);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "*FCM 토큰 갱신", description = "FCM 토큰을 갱신")
    @PostMapping("/fcm")
    public ResponseEntity<SuccessResponse> changeFcm(@CurrentUserId Long userId,
                                                     @RequestBody AutoLoginRequest dto) {
        authService.changeFcm(userId, dto.getFcm());
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*로그아웃", description = "사용자의 FCM 토큰 삭제")
    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse> logout(@CurrentUserId Long userId) {
        authService.logout(userId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

}
