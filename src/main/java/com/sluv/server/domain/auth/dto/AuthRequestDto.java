package com.sluv.server.domain.auth.dto;

import com.sluv.server.domain.auth.SnsType;
import lombok.Data;

@Data
public class AuthRequestDto {
    private String accessToken;
    private SnsType snsType;
}
