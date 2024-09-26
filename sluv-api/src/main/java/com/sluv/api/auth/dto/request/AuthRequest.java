package com.sluv.api.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@NoArgsConstructor
public class AuthRequest {
    @Schema(description = "소셜에서 발급한 AccessToken")
    private String accessToken;
    @Schema(description = "SNS 타입 이름")
    private String snsType;
    @Nullable
    @Schema(description = "FCM 토큰")
    private String fcm;
}
