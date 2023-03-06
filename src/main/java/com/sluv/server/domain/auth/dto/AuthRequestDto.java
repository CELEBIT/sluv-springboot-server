package com.sluv.server.domain.auth.dto;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String accessToken;
    private String snsType;
}
