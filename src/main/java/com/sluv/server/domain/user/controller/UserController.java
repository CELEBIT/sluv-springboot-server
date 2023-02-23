package com.sluv.server.domain.user.controller;

import com.sluv.server.domain.user.dto.UserDto;
import com.sluv.server.domain.user.service.UserService;
import com.sluv.server.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @GetMapping("/")
    public String elseLogin(){
        return "hi";
    }

    @GetMapping("/login")
    public String testLogin(){
        return "hi security";
    }

    @GetMapping("/jwtCreateTest")
    public String testCreateToken(@RequestBody UserDto dto){

        return jwtProvider.createAccessToken(dto);
    }

    @GetMapping("/jwtSolveTest")
    public String testSolveToken(HttpServletRequest request){

        return userService.getUserIdByToken(request);
    }
}
