package com.sluv.server.domain.user.service;

import static com.sluv.server.fixture.UserFixture.구글_유저_생성;
import static com.sluv.server.fixture.UserFixture.애플_유저_생성;
import static com.sluv.server.fixture.UserFixture.카카오_유저_생성;
import static org.assertj.core.api.Assertions.assertThat;

import com.sluv.server.domain.user.dto.UserSearchInfoDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.enums.UserStatus;
import com.sluv.server.domain.user.repository.FollowRepository;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.common.response.PaginationResDto;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
public class FollowServiceTest {
    @Autowired
    private FollowService followService;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clear() {
        followRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("팔로우를 등록한다.")
    @Test
    void saveUserFollowTest() {
        //given
        User user1 = 애플_유저_생성();
        User user2 = 구글_유저_생성();
        userRepository.saveAll(List.of(user1, user2));

        //when
        followService.postUserFollow(user1, user2.getId());

        //then
        assertThat(followRepository.findAll()).hasSize(1);
    }

    @DisplayName("팔로우를 삭제한다.")
    @Test
    void deleteUserFollowTest() {
        //given
        User user1 = 카카오_유저_생성();
        User user2 = 구글_유저_생성();
        userRepository.saveAll(List.of(user1, user2));

        //when
        followService.postUserFollow(user1, user2.getId());
        followService.postUserFollow(user1, user2.getId());

        //then
        assertThat(followRepository.findAll()).hasSize(0);
    }

    @DisplayName("팔로워를 조회한다.")
    @Test
    void getUserFollowerTest() {
        //given
        User user1 = 카카오_유저_생성();
        User user2 = 구글_유저_생성();
        User user3 = 애플_유저_생성();
        user2.changeUserStatus(UserStatus.ACTIVE);
        user3.changeUserStatus(UserStatus.ACTIVE);
        user2.changeNickname("팔로워1");
        user3.changeNickname("팔로워2");

        userRepository.saveAll(List.of(user1, user2, user3));
        followService.postUserFollow(user2, user1.getId());
        followService.postUserFollow(user3, user1.getId());

        //when
        PaginationResDto<UserSearchInfoDto> userFollowers = followService.getUserFollower(user1, user1.getId(),
                PageRequest.of(0, 1));

        //then
        assertThat(userFollowers.getContent().get(0)).extracting("nickName").isEqualTo("팔로워2");
        assertThat(userFollowers.getHasNext()).isEqualTo(true);
    }

    @DisplayName("팔로잉 유저를 조회한다.")
    @Test
    void getUserFollowingTest() {
        //given
        User user1 = 카카오_유저_생성();
        User user2 = 구글_유저_생성();
        User user3 = 애플_유저_생성();
        user2.changeUserStatus(UserStatus.ACTIVE);
        user3.changeUserStatus(UserStatus.ACTIVE);
        user2.changeNickname("유저2");
        user3.changeNickname("유저3");

        userRepository.saveAll(List.of(user1, user2, user3));
        followService.postUserFollow(user1, user2.getId());
        followService.postUserFollow(user1, user3.getId());

        //when
        PaginationResDto<UserSearchInfoDto> userFollowers = followService.getUserFollowing(user1, user1.getId(),
                PageRequest.of(0, 1));

        //then
        assertThat(userFollowers.getContent().get(0)).extracting("nickName").isEqualTo("유저3");
        assertThat(userFollowers.getHasNext()).isEqualTo(true);
    }

}
