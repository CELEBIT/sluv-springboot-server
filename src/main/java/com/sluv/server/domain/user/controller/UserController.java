package com.sluv.server.domain.user.controller;


import com.sluv.server.domain.user.service.UserService;
import com.sluv.server.global.common.BaseResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<?> getUserId(HttpServletRequest request) {

            return ResponseEntity.ok().body(new BaseResponse<>(userService.getUserIdByToken(request)));

    }
}
