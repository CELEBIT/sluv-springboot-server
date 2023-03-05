package com.sluv.server.domain.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class SocialUserInfoDto {
    private String email;
    private String profileImgUrl;

    private String ageRange;

    private String gender;

    @Builder
    public SocialUserInfoDto(String email, String profileImgUrl, String ageRange, String gender) {
        this.email = email;
        this.profileImgUrl = profileImgUrl;
        this.ageRange = ageRange;
        this.gender = gender;
    }
}
