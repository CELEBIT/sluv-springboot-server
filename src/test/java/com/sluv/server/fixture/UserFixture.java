package com.sluv.server.fixture;

import com.sluv.server.domain.auth.dto.SocialUserInfoDto;
import com.sluv.server.domain.auth.enums.SnsType;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.enums.UserAge;
import com.sluv.server.domain.user.enums.UserGender;

public class UserFixture {

    public static User 카카오유저1_생성() {
        SocialUserInfoDto userSocialInfo = SocialUserInfoDto.builder()
                .email("user1@email.com")
                .profileImgUrl("http://user1.com")
                .ageRange(UserAge.UNKNOWN)
                .gender(UserGender.UNKNOWN)
                .build();

        return User.toEntity(userSocialInfo, SnsType.KAKAO);
    }
}
