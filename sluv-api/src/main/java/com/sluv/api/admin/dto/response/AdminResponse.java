package com.sluv.api.admin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminResponse {
    @Schema(description = "Admin 이메일")
    private String email;
    @Schema(description = "Encodeing 된 Admin 비밀번호")
    private String encodesPassword;

    public static AdminResponse of(String email, String encodedPassword) {
        return new AdminResponse(email, encodedPassword);
    }
}
