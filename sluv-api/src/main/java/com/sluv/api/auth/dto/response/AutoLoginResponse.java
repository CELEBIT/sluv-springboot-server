package com.sluv.api.auth.dto.response;

import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.enums.UserStatus;
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
public class AutoLoginResponse {
    @Schema(description = "유저 Status")
    private UserStatus userStatus;

    public static AutoLoginResponse of(User user) {
        return AutoLoginResponse.builder()
                .userStatus(user.getUserStatus())
                .build();
    }
}