package com.sluv.domain.auth.dto;

import com.sluv.domain.user.enums.UserAge;
import com.sluv.domain.user.enums.UserGender;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class SocialUserInfoDto {
    private String email;
    private String profileImgUrl;
    private UserAge ageRange;
    private UserGender gender;

    @Builder
    public SocialUserInfoDto(String email, String profileImgUrl, UserAge ageRange, UserGender gender) {
        this.email = email;
        this.profileImgUrl = profileImgUrl;
        this.ageRange = ageRange;
        this.gender = gender;
    }

}
