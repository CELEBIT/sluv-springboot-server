package com.sluv.server.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class SocialUserInfoDto {
    @Schema(description = "사용자의 Email")
    private String email;
    @Schema(description = "사용자의 프로필 사진 URL")
    private String profileImgUrl;
    @Schema(description = "사용자의 연령대")
    private String ageRange;
    @Schema(description = "사용자의 성별")
    private String gender;

    @Builder
    public SocialUserInfoDto(String email, String profileImgUrl, String ageRange, String gender) {
        this.email = email;
        this.profileImgUrl = profileImgUrl;
        this.ageRange = ageRange;
        this.gender = gender;
    }
}
