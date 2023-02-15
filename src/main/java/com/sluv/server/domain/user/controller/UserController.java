package com.sluv.server.domain.user.controller;

import com.sluv.server.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("/")
    public String elseLogin(){
        return "hi";
    }

    @GetMapping("/login")
    public String testLogin(){
        return "hi security";
    }
}
