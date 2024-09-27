package com.sluv.api.alarm.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AlarmCheckResponse {

    private Boolean isAllRead;

    public static AlarmCheckResponse of(Boolean isAllRead) {
        return AlarmCheckResponse.builder()
                .isAllRead(isAllRead)
                .build();
    }

}
