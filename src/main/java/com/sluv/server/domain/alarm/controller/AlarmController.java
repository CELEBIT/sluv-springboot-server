package com.sluv.server.domain.alarm.controller;

import com.sluv.server.domain.alarm.dto.AlarmResponse;
import com.sluv.server.domain.alarm.service.AlarmService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.PaginationResDto;
import com.sluv.server.global.common.response.SuccessDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/app/alarm")
@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<AlarmResponse>>> getAlarms(
            @AuthenticationPrincipal User user, Pageable pageable) {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<AlarmResponse>>builder()
                        .result(alarmService.getAlarmsByUserId(user.getId(), pageable))
                        .build()
        );
    }

}
