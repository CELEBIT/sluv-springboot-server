package com.sluv.server.domain.auth.dto;

import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "자동로그인 응답")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoLoginResponseDto {
    @Schema(description = "유저 Status")
    private UserStatus userStatus;

    public static AutoLoginResponseDto of(User user) {
        return AutoLoginResponseDto.builder()
                .userStatus(user.getUserStatus())
                .build();
    }
}