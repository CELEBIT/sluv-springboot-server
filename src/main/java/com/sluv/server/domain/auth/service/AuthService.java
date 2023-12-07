package com.sluv.server.domain.auth.service;

import com.sluv.server.domain.auth.dto.AuthResponseDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;

    public AuthResponseDto jwtTestService(User user) {

        return AuthResponseDto.builder()
                .token(jwtProvider.createAccessToken(user))
                .build();
    }
}
