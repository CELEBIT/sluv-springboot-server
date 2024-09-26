package com.sluv.api.user.dto;

import com.sluv.domain.user.dto.UserInfoDto;
import com.sluv.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMypageResDto {
    @Schema(description = "해당 유저의 정보")
    private UserInfoDto userInfo;
    @Schema(description = "현재 유저 -> 해당 유저 팔로워 여부")
    private Boolean followStatus;
    @Schema(description = "해당 유저의 팔로워 수")
    private Long followerCount;
    @Schema(description = "해당 유저의 팔로잉 수")
    private Long followingCount;

    // 현재 유저의 MyPage
    @Schema(description = "해당 유저가 작성한 아이템 개수")
    private Long itemCount;
    @Schema(description = "해당 유저가 최근 작성한 아이템 2개의 대표이미지")
    private List<String> imgList;
    @Schema(description = "해당 유저가 작성한 Question + Comment 개수")
    private Long communityCount;

    public static UserMypageResDto of(User user,
                                      Boolean followStatus,
                                      Long followerCount,
                                      Long followingCount,
                                      Long itemCount,
                                      List<String> imgList,
                                      Long communityCount
    ) {

        return UserMypageResDto.builder()
                .userInfo(UserInfoDto.of(user))
                .followStatus(followStatus)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .itemCount(itemCount)
                .imgList(imgList)
                .communityCount(communityCount)
                .build();
    }
}
