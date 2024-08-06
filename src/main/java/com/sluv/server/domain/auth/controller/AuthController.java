package com.sluv.server.domain.auth.controller;

import com.sluv.server.domain.auth.dto.AuthRequestDto;
import com.sluv.server.domain.auth.dto.AuthResponseDto;
import com.sluv.server.domain.auth.dto.AutoLoginRequestDto;
import com.sluv.server.domain.auth.dto.AutoLoginResponseDto;
import com.sluv.server.domain.auth.enums.SnsType;
import com.sluv.server.domain.auth.service.AppleUserService;
import com.sluv.server.domain.auth.service.AuthService;
import com.sluv.server.domain.auth.service.GoogleUserService;
import com.sluv.server.domain.auth.service.KakaoUserService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.cache.CacheService;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KakaoUserService kakaoUserService;
    private final GoogleUserService googleUserService;
    private final AppleUserService appleUserService;
    private final AuthService authService;
    private final CacheService cacheService;


    @Operation(summary = "소셜 로그인", description = "KAKAO:[AccessToken], GOOGLE, APPLE: [IdToken]")
    @PostMapping("/social-login")
    public ResponseEntity<SuccessDataResponse<AuthResponseDto>> socialLogin(@RequestBody AuthRequestDto request)
            throws Exception {
        User loginUser = null;

        SnsType userSnsType = SnsType.fromString(request.getSnsType());

        switch (userSnsType) {
            case KAKAO -> loginUser = kakaoUserService.kakaoLogin(request);
            case GOOGLE -> loginUser = googleUserService.googleLogin(request);
            case APPLE -> loginUser = appleUserService.appleLogin(request);
        }
        cacheService.visitMember(loginUser.getId());
        AuthResponseDto authResponseDto = authService.getAuthResDto(loginUser);

        return ResponseEntity.ok().body(SuccessDataResponse.<AuthResponseDto>builder()
                .result(authResponseDto)
                .build()
        );
    }

    @Operation(summary = "*자동 로그인", description = "토큰 만료 시 error code : 4002")
    @GetMapping("/auto-login")
    public ResponseEntity<SuccessDataResponse<AutoLoginResponseDto>> autoLogin(@AuthenticationPrincipal User user) {
        cacheService.visitMember(user.getId());
        authService.checkFcm(user.getId());
        return ResponseEntity.ok().body(
                SuccessDataResponse.<AutoLoginResponseDto>builder()
                        .result(AutoLoginResponseDto.of(user))
                        .build()
        );
    }

    @Operation(summary = "*FCM 토큰 갱신", description = "FCM 토큰을 갱신")
    @PostMapping("/fcm")
    public ResponseEntity<SuccessResponse> changeFcm(@AuthenticationPrincipal User user,
                                                     @RequestBody AutoLoginRequestDto dto) {
        authService.changeFcm(user.getId(), dto.getFcm());
        return ResponseEntity.ok().body(new SuccessResponse());
    }
}
