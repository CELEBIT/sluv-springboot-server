package com.sluv.server.domain.auth.dto;

import com.sluv.server.domain.user.enums.UserAge;
import com.sluv.server.domain.user.enums.UserGender;
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
    private UserAge ageRange;
    @Schema(description = "사용자의 성별")
    private UserGender gender;

    @Builder
    public SocialUserInfoDto(String email, String profileImgUrl, UserAge ageRange, UserGender gender) {
        this.email = email;
        this.profileImgUrl = profileImgUrl;
        this.ageRange = ageRange;
        this.gender = gender;
    }
}
