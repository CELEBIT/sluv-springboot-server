package com.sluv.api.user.controller;

import com.sluv.api.common.response.SuccessResponse;
import com.sluv.api.user.service.UserBlockService;
import com.sluv.common.annotation.CurrentUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/user")
@RequiredArgsConstructor
public class UserBlockController {

    private final UserBlockService userBlockService;

    @PostMapping("/{userId}/block")
    public ResponseEntity<SuccessResponse> postUserBlock(@CurrentUserId Long userId,
                                                         @PathVariable(name = "userId") Long targetId) {
        userBlockService.postUserBlock(userId, targetId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

}
