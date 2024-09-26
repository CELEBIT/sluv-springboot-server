package com.sluv.api.auth.dto.response;

import com.sluv.domain.user.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "유저토큰 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AuthResponse {

    @Schema(description = "유저 엑세스 토큰")
    private String token;
    @Schema(description = "유저 Status")
    private UserStatus userStatus;

    public static AuthResponse of(String token, UserStatus status) {
        return AuthResponse.builder()
                .token(token)
                .userStatus(status)
                .build();
    }

}
