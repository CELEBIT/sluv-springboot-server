package com.sluv.domain.user.dto;

import com.sluv.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBlockDto {
    /**
     * 사용자 차단 리스트
     */
    @Schema(description = "사용자 Id")
    private Long id;
    @Schema(description = "사용자 Nickname")
    private String nickName;
    @Schema(description = "사용자 이미지 URL")
    private String profileImgUrl;
    @Schema(description = "현재 사용자의 차단 여부")
    private Boolean blockStatus;

    public static UserBlockDto of(User user, Boolean blockStatus) {
        return UserBlockDto.builder()
                .id(user.getId())
                .nickName(user.getNickname())
                .profileImgUrl(user.getProfileImgUrl())
                .blockStatus(blockStatus)
                .build();
    }
}
