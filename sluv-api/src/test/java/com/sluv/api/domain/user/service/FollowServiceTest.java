package com.sluv.api.domain.user.service;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.user.service.FollowService;
import com.sluv.domain.user.dto.UserSearchInfoDto;
import com.sluv.domain.user.entity.Follow;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.enums.UserStatus;
import com.sluv.domain.user.service.FollowDomainService;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.infra.alarm.service.UserAlarmService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.sluv.api.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FollowServiceTest {

    @InjectMocks
    private FollowService followService;

    @Mock
    private FollowDomainService followDomainService;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private UserAlarmService userAlarmService;

    @DisplayName("팔로우를 등록한다.")
    @Test
    void saveUserFollowTest() {
        //given
        User follower = 애플_유저_생성().toBuilder().id(1L).build();
        User followee = 구글_유저_생성().toBuilder().id(2L).build();
        when(userDomainService.findById(eq(follower.getId()))).thenReturn(follower);
        when(userDomainService.findById(eq(followee.getId()))).thenReturn(followee);
        when(followDomainService.getFollowStatus(any(User.class), any(Long.class))).thenReturn(false);

        //when
        followService.postUserFollow(follower.getId(), followee.getId());

        //then
        verify(followDomainService).saveFollow(any(Follow.class));
        verify(userAlarmService).sendAlarmAboutFollow(any(Long.class), any(Long.class));
    }

    @DisplayName("팔로우를 삭제한다.")
    @Test
    void deleteUserFollowTest() {
        //given
        User follower = 애플_유저_생성().toBuilder().id(1L).build();
        User followee = 구글_유저_생성().toBuilder().id(2L).build();
        when(userDomainService.findById(eq(follower.getId()))).thenReturn(follower);
        when(userDomainService.findById(eq(followee.getId()))).thenReturn(followee);
        when(followDomainService.getFollowStatus(any(User.class), any(Long.class))).thenReturn(true);

        //when
        followService.postUserFollow(follower.getId(), followee.getId());

        //then
        verify(followDomainService).deleteFollow(eq(follower), eq(followee));
    }

    @DisplayName("팔로워를 조회한다.")
    @Test
    void getUserFollowerTest() {
        //given
        String followeeNickname = "팔로위";
        String followerNickname = "팔로워";
        User nowUser = 카카오_유저_생성().toBuilder().id(1L).userStatus(UserStatus.ACTIVE).build();
        User targetUser = 구글_유저_생성().toBuilder().id(2L).userStatus(UserStatus.ACTIVE).nickname(followeeNickname).build();
        User follower = 애플_유저_생성().toBuilder().id(3L).userStatus(UserStatus.ACTIVE).nickname(followerNickname).build();

        UserSearchInfoDto response = UserSearchInfoDto.of(follower, false, false);
        Page<User> userPage = new PageImpl<>(List.of(follower));

        when(userDomainService.findById(eq(nowUser.getId()))).thenReturn(nowUser);
        when(userDomainService.getAllFollower(eq(targetUser.getId()), any(Pageable.class))).thenReturn(userPage);
        when(followDomainService.getUserSearchInfoDto(eq(nowUser), any(List.class), eq("follower"))).thenReturn(List.of(response));

        //when
        PaginationResponse<UserSearchInfoDto> userFollowers = followService.getUserFollower(nowUser.getId(), targetUser.getId(),
                PageRequest.of(0, 1));

        //then
        assertThat(userFollowers.getContent().get(0)).extracting("nickName").isEqualTo(followerNickname);
    }

    @DisplayName("팔로잉 유저를 조회한다.")
    @Test
    void getUserFollowingTest() {
        //given
        String followeeNickname = "팔로위";
        String followerNickname = "팔로워";
        User nowUser = 카카오_유저_생성().toBuilder().id(1L).userStatus(UserStatus.ACTIVE).build();
        User targetUser = 구글_유저_생성().toBuilder().id(2L).userStatus(UserStatus.ACTIVE).nickname(followeeNickname).build();
        User follower = 애플_유저_생성().toBuilder().id(3L).userStatus(UserStatus.ACTIVE).nickname(followerNickname).build();

        UserSearchInfoDto response = UserSearchInfoDto.of(follower, false, false);
        Page<User> userPage = new PageImpl<>(List.of(follower));

        when(userDomainService.findById(eq(nowUser.getId()))).thenReturn(nowUser);
        when(userDomainService.getAllFollowing(eq(targetUser.getId()), any(Pageable.class))).thenReturn(userPage);
        when(followDomainService.getUserSearchInfoDto(eq(nowUser), any(List.class), eq("followee"))).thenReturn(List.of(response));

        //when
        PaginationResponse<UserSearchInfoDto> userFollowers = followService.getUserFollowing(nowUser.getId(), targetUser.getId(),
                PageRequest.of(0, 1));

        //then
        assertThat(userFollowers.getContent().get(0)).extracting("nickName").isEqualTo(followerNickname);
    }

}
