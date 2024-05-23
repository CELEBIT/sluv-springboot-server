package com.sluv.server.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminResDto {
    @Schema(description = "Admin 이메일")
    private String email;
    @Schema(description = "Encodeing 된 Admin 비밀번호")
    private String encodesPassword;

    public static AdminResDto of(String email, String encodedPassword) {
        return new AdminResDto(email, encodedPassword);
    }
}
