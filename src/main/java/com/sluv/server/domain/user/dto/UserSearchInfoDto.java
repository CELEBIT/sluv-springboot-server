package com.sluv.server.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSearchInfoDto {
    /**
     * 키워드를 통한 유저 검색시 Response Dto
     */
    @Schema(description = "사용자 Id")
    private Long id;
    @Schema(description = "사용자 Nickname")
    private String nickName;
    @Schema(description = "사용자 이미지 URL")
    private String profileImgUrl;
    @Schema(description = "현재 사용자의 팔로우 여부")
    private Boolean followStatus;
}
