package com.sluv.api.user.controller;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.common.response.SuccessResponse;
import com.sluv.api.user.service.UserBlockService;
import com.sluv.common.annotation.CurrentUserId;
import com.sluv.domain.user.dto.UserBlockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/block")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<UserBlockDto>>> getUserBlock(@CurrentUserId Long userId,
                                                                                              Pageable pageable) {
        PaginationResponse<UserBlockDto> response = userBlockService.getUserBlock(userId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

}
