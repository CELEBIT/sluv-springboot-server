package com.sluv.domain.user.dto;

import com.sluv.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWithFollowerCountDto {
    private Long id;
    private String nickName;
    private String imgUrl;
    private Long followerCount;

    public static UserWithFollowerCountDto of(User user, Long followerCount) {
        return UserWithFollowerCountDto.builder()
                .id(user.getId())
                .nickName(user.getNickname())
                .imgUrl(user.getProfileImgUrl())
                .followerCount(followerCount)
                .build();
    }
}
