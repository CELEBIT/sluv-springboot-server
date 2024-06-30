package com.sluv.server.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AutoLoginRequestDto {

    @Schema(description = "FCM 토큰")
    private String fcm;
}
