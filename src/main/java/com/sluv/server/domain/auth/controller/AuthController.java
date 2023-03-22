package com.sluv.server.domain.auth.controller;

import com.sluv.server.domain.auth.enums.SnsType;
import com.sluv.server.domain.auth.dto.AuthRequestDto;
import com.sluv.server.domain.auth.dto.AuthResponseDto;
import com.sluv.server.domain.auth.service.AppleUserService;
import com.sluv.server.domain.auth.service.AuthService;
import com.sluv.server.domain.auth.service.GoogleUserService;
import com.sluv.server.domain.auth.service.KakaoUserService;

import com.sluv.server.domain.user.dto.UserDto;
import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import com.sluv.server.global.jwt.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KakaoUserService kakaoUserService;
    private final GoogleUserService googleUserService;
    private final AppleUserService appleUserService;
    private final AuthService authService;

    private final JwtProvider jwtProvider;

    @Operation(
            summary = "소셜 로그인",
            description = "AccessToken과 sysType로 로그인."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "2000", description = "존재하지 않는 유저", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "2001", description = "지원하지 않는 SNS Type", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "4001", description = "유효하지 않는 토큰", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "4002", description = "만료된 토큰", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })

    @PostMapping("/social-login")
    public ResponseEntity<SuccessDataResponse<AuthResponseDto>> socialLogin(@RequestBody AuthRequestDto request) throws Exception {
        AuthResponseDto response = null;

        SnsType userSnsType = SnsType.fromString(request.getSnsType());

        switch (userSnsType) {
            case KAKAO -> response = kakaoUserService.kakaoLogin(request);
            case GOOGLE -> response = googleUserService.googleLogin(request);
            case APPLE -> response = appleUserService.appleLogin(request);
        }

        return ResponseEntity.ok().body(SuccessDataResponse.<AuthResponseDto>builder()
                                                            .result(response)
                                                            .build()
                                                      );
    }

    @GetMapping("/auto-login")
    public ResponseEntity<SuccessResponse> autoLogin(HttpServletRequest request){
        String accessToken = jwtProvider.resolveToken(request);
        jwtProvider.validateToken(accessToken);

        return ResponseEntity.ok().body(new SuccessResponse());
    }

    @PostMapping("/test")
    public ResponseEntity<?> testToken(@RequestBody UserDto dto){

        return ResponseEntity.ok().body(SuccessDataResponse.builder()
                                                        .result(authService.jwtTestService(dto))
                                                        .build()
                                    );
    }

}
