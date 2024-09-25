package com.sluv.api.user.controller;

import com.sluv.api.common.response.SuccessResponse;
import com.sluv.api.user.dto.UserReportReqDto;
import com.sluv.api.user.service.UserReportService;
import com.sluv.common.annotation.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/app/user")
@RequiredArgsConstructor
public class UserReportController {

    private final UserReportService userReportService;

    @Operation(summary = "*유저 신고하기", description = "User 토큰 필요")
    @PostMapping("/{userId}/report")
    public ResponseEntity<SuccessResponse> postUserReport(@CurrentUserId Long userId,
                                                          @PathVariable(name = "userId") Long targetId,
                                                          @RequestBody UserReportReqDto dto) {
        userReportService.postUserReport(userId, targetId, dto);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }
}
