package com.sluv.server.domain.alarm.controller;

import com.sluv.server.domain.alarm.dto.AlarmResponse;
import com.sluv.server.domain.alarm.service.AlarmService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.PaginationResDto;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PatchMapping("/read")
    public ResponseEntity<SuccessResponse> pathAlarmStatusToRead(@AuthenticationPrincipal User user,
                                                                 @RequestParam Long alarmId) {
        alarmService.patchAlarmStatusToRead(user, alarmId);
        return ResponseEntity.ok().body(new SuccessResponse());
    }

    @DeleteMapping("/{alarmId}")
    public ResponseEntity<SuccessResponse> deleteAlarm(@AuthenticationPrincipal User user,
                                                       @PathVariable("alarmId") Long alarmId) {
        alarmService.deleteAlarm(user, alarmId);
        return ResponseEntity.ok().body(new SuccessResponse());
    }

    @DeleteMapping("/all")
    public ResponseEntity<SuccessResponse> deleteAllAlarm(@AuthenticationPrincipal User user) {
        alarmService.deleteAllAlarm(user);
        return ResponseEntity.ok().body(new SuccessResponse());
    }

}
