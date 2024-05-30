package com.sluv.server.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserTokenResDto {
    @Schema(description = "유저 엑세스 토큰")
    private String token;

    public static AdminUserTokenResDto from(String token) {
        return new AdminUserTokenResDto(token);
    }
}
