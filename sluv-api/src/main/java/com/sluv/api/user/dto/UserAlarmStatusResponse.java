package com.sluv.api.user.dto;

import com.sluv.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAlarmStatusResponse {
    @Schema(description = "알람 수신동의 상태")
    private Boolean alarmStatus;

    public static UserAlarmStatusResponse from(User user) {
        return UserAlarmStatusResponse.builder()
                .alarmStatus(user.getAlarmStatus())
                .build();
    }

}
