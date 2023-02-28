package com.sluv.server.domain.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sluv.server.domain.auth.dto.AuthRequestDto;
import com.sluv.server.domain.auth.dto.AuthResponseDto;
import com.sluv.server.domain.auth.service.KakaoUserService;
import com.sluv.server.domain.user.dto.UserDto;

import com.sluv.server.global.common.BaseResponse;
import com.sluv.server.global.jwt.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KakaoUserService kakaoUserService;
    private final JwtProvider jwtProvider;

    @Operation(
            summary = "소셜 로그인",
            description = "AccessToken과 sysType로 로그인."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "3000", description = "존재하지 않는 유저"),
            @ApiResponse(responseCode = "3001", description = "유효하지 않는 토큰")
    })

    @GetMapping("/social-login")
    public ResponseEntity<?> socialLogin(@RequestBody AuthRequestDto request) throws JsonProcessingException {
        AuthResponseDto response = null;
            switch (request.getSnsType()) {
                case KAKAO -> response = kakaoUserService.kakaoLogin(request);
                case GOOGLE -> System.out.println("구글");
                case APPLE -> System.out.println("애플");
            }

            return ResponseEntity.ok().body(new BaseResponse<>(response));
    }

    @GetMapping("/test")
    public String testToken(@RequestBody UserDto dto){
        return jwtProvider.createAccessToken(dto);
    }


}
