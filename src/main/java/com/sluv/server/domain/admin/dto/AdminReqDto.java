package com.sluv.server.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminReqDto {
    @Schema(description = "Admin 이메일")
    private String email;
    @Schema(description = "Admin 비밀번호")
    private String password;
}
