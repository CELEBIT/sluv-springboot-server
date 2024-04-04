package com.sluv.server.domain.user.dto;

import com.sluv.server.domain.auth.enums.SnsType;
import com.sluv.server.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSocialDto implements Serializable {
    @Schema(description = "사용자 Email")
    private String email;
    @Schema(description = "사용자 Social 종류")
    private SnsType snsType;

    public static UserSocialDto of(User user) {
        return UserSocialDto.builder()
                .email(user.getEmail())
                .snsType(user.getSnsType())
                .build();
    }
}
