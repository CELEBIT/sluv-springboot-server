package com.sluv.server.domain.user.service;

import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public String getUserIdByToken(HttpServletRequest request){
        String token = jwtProvider.resolveToken(request);
        if (jwtProvider.validateToken(token)){
            System.out.println("x = " + token);
            return jwtProvider.getUserId(token).toString();
        }else{
            return "expired";
        }

    }
}
