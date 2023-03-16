package com.sluv.server.domain.user.controller;


import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.service.UserService;
import com.sluv.server.global.common.response.SuccessDataResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/celeb")
    public ResponseEntity<SuccessDataResponse<List<Celeb>>> getInterestedCeleb(@AuthenticationPrincipal User user){


        return ResponseEntity.ok().body(SuccessDataResponse.<List<Celeb>>builder()
                                                            .result(userService.findUserInterestedCeleb(user))
                                                            .build());
    }


}
