package com.sluv.api.fixture;

import com.sluv.domain.auth.dto.SocialUserInfoDto;
import com.sluv.domain.auth.enums.SnsType;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.enums.UserAge;
import com.sluv.domain.user.enums.UserGender;

public class UserFixture {

    public static User 카카오_유저_생성() {
        SocialUserInfoDto userSocialInfo = SocialUserInfoDto.builder()
                .email("kakaoUser@email.com")
                .profileImgUrl("http://kakaoUser.com")
                .ageRange(UserAge.UNKNOWN)
                .gender(UserGender.UNKNOWN)
                .build();

        return User.toEntity(userSocialInfo, SnsType.KAKAO, "testfcm");
    }

    public static User 구글_유저_생성() {
        SocialUserInfoDto userSocialInfo = SocialUserInfoDto.builder()
                .email("googleUser@email.com")
                .profileImgUrl("http://googleUser.com")
                .ageRange(UserAge.UNKNOWN)
                .gender(UserGender.UNKNOWN)
                .build();

        return User.toEntity(userSocialInfo, SnsType.GOOGLE, "testfcm");
    }

    public static User 애플_유저_생성() {
        SocialUserInfoDto userSocialInfo = SocialUserInfoDto.builder()
                .email("appleUser@email.com")
                .profileImgUrl("http://appleUser.com")
                .ageRange(UserAge.UNKNOWN)
                .gender(UserGender.UNKNOWN)
                .build();

        return User.toEntity(userSocialInfo, SnsType.APPLE, "testfcm");
    }
}
