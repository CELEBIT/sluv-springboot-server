package com.sluv.server.domain.user.dto;

import com.sluv.server.domain.celeb.dto.InterestedCelebResDto;
import com.sluv.server.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    @Schema(description = "해당 유저의 관심 셀럽 리스트")
    private List<InterestedCelebResDto> interestedCelebList;

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
                                      List<InterestedCelebResDto> interestedCelebList,
                                      Long itemCount,
                                      List<String> imgList,
                                      Long communityCount
                                      ){

        return UserMypageResDto.builder()
                .userInfo(UserInfoDto.of(user))
                .followStatus(followStatus)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .interestedCelebList(interestedCelebList)
                .itemCount(itemCount)
                .imgList(imgList)
                .communityCount(communityCount)
                .build();
    }
}
