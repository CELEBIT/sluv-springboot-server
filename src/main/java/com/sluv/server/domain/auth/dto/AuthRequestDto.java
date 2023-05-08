package com.sluv.server.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuthRequestDto {
    @Schema(description = "소셜에서 발급한 AccessToken")
    private String accessToken;
    @Schema(description = "SNS 타입 이름")
    private String snsType;
}
