package com.sluv.api.alarm.controller;

import com.sluv.api.alarm.dto.AlarmCheckResponse;
import com.sluv.api.alarm.dto.AlarmResponse;
import com.sluv.api.alarm.service.AlarmService;
import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.common.response.SuccessResponse;
import com.sluv.common.annotation.CurrentUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/app/alarm")
@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<AlarmResponse>>> getAlarms(
            @CurrentUserId Long userId, Pageable pageable) {
        PaginationResponse<AlarmResponse> response = alarmService.getAlarmsByUserId(userId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @PatchMapping("/read")
    public ResponseEntity<SuccessResponse> pathAlarmStatusToRead(@CurrentUserId Long userId,
                                                                 @RequestParam Long alarmId) {
        alarmService.patchAlarmStatusToRead(userId, alarmId);

        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @DeleteMapping("/{alarmId}")
    public ResponseEntity<SuccessResponse> deleteAlarm(@CurrentUserId Long userId,
                                                       @PathVariable("alarmId") Long alarmId) {
        alarmService.deleteAlarm(userId, alarmId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @DeleteMapping("/all")
    public ResponseEntity<SuccessResponse> deleteAllAlarm(@CurrentUserId Long userId) {
        alarmService.deleteAllAlarm(userId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @GetMapping("/check")
    public ResponseEntity<SuccessDataResponse<AlarmCheckResponse>> checkAlarmAllRead(@CurrentUserId Long userId) {
        AlarmCheckResponse response = alarmService.checkAlarmAllRead(userId);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

}
