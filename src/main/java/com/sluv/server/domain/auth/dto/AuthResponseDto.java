package com.sluv.server.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "유저토큰 응답")
@Data
public class AuthResponseDto {
    @Schema(description = "유저 엑세스 토큰")
    private String token;

    @Builder
    public AuthResponseDto(String token) {
        this.token = token;
    }
}
