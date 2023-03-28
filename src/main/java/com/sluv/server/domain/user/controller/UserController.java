package com.sluv.server.domain.user.controller;


import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.item.dto.CelebParentResponseDto;
import com.sluv.server.domain.item.dto.CelebResponseDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.service.UserService;
import com.sluv.server.global.common.response.SuccessDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/celeb")
    public ResponseEntity<SuccessDataResponse<List<CelebParentResponseDto>>> getInterestedCeleb(@AuthenticationPrincipal User user){

        return ResponseEntity.ok().body(SuccessDataResponse.<List<CelebParentResponseDto>>builder()
                                                            .result(userService.getInterestedCeleb(user))
                                                            .build());
    }


}
