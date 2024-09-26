package com.sluv.api.admin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserTokenResponse {
    @Schema(description = "유저 엑세스 토큰")
    private String token;

    public static AdminUserTokenResponse from(String token) {
        return new AdminUserTokenResponse(token);
    }
}
