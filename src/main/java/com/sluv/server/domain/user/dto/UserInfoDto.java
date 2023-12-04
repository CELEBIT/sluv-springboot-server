package com.sluv.server.domain.user.dto;

import com.sluv.server.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDto {
    @Schema(description = "사용자 Id")
    private Long id;
    @Schema(description = "사용자 Nickname")
    private String nickName;
    @Schema(description = "사용자 이미지 URL")
    private String profileImgUrl;

    public static UserInfoDto of(User user) {
        return UserInfoDto.builder()
                .id(user.getId())
                .nickName(user.getNickname())
                .profileImgUrl(user.getProfileImgUrl())
                .build();
    }
}
