package com.sluv.api.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AutoLoginRequest {

    @Schema(description = "FCM 토큰")
    private String fcm;
}
