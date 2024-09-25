package com.sluv.domain.user.dto;

import com.sluv.domain.user.entity.User;
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
public class UserInfoDto implements Serializable {
    @Schema(description = "사용자 Id")
    private Long id;
    @Schema(description = "사용자 Nickname")
    private String nickName;
    @Schema(description = "사용자 이미지 URL")
    private String profileImgUrl;

    public static UserInfoDto of(User user) {
        if (user == null) {
            return UserInfoDto.builder()
                    .id(-1L)
                    .build();
        }

        return UserInfoDto.builder()
                .id(user.getId())
                .nickName(user.getNickname())
                .profileImgUrl(user.getProfileImgUrl())
                .build();
    }
}
