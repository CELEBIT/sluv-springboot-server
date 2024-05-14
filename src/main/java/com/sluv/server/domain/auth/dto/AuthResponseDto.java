package com.sluv.server.domain.auth.dto;

import com.sluv.server.domain.user.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "유저토큰 응답")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {
    @Schema(description = "유저 엑세스 토큰")
    private String token;
    @Schema(description = "유저 Status")
    private UserStatus userStatus;
}
