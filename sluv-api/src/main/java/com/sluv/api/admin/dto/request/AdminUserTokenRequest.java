package com.sluv.api.admin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminUserTokenRequest {
    @Schema(description = "Admin 이메일")
    private String email;
    @Schema(description = "Admin 비밀번호")
    private String password;
    @Schema(description = "토큰을 얻을 유저의 Id")
    private Long userId;
}
