package com.sluv.server.domain.user.service;

import com.sluv.server.domain.user.dto.UserSearchInfoDto;
import com.sluv.server.domain.user.entity.Follow;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.FollowRepository;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.common.response.PaginationResDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Transactional
    public void postUserFollow(User user, Long userId) {
        // target이 될 유저 검색
        User targetUser = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // Follow 여부 확인.
        Boolean followStatus = followRepository.getFollowStatus(user, targetUser);

        if (followStatus) {
            followRepository.deleteFollow(user, targetUser);
        } else {
            // Follow 정보 등록.
            followRepository.save(Follow.toEntity(user, targetUser));
        }

    }

    @Transactional(readOnly = true)
    public PaginationResDto<UserSearchInfoDto> getUserFollower(User user, Long userId, Pageable pageable) {
        // Follower 들 조회
        Page<User> followerPage = userRepository.getAllFollower(userId, pageable);

        // UserSearchInfoDto로 가공
        List<UserSearchInfoDto> content =
                followRepository.getUserSearchInfoDto(user, followerPage.getContent(), "follower");

        return PaginationResDto.of(followerPage, content);
    }

    @Transactional(readOnly = true)
    public PaginationResDto<UserSearchInfoDto> getUserFollowing(User user, Long userId, Pageable pageable) {
        // Following 들 조회
        Page<User> followerPage = userRepository.getAllFollowing(userId, pageable);

        // UserSearchInfoDto로 가공
        List<UserSearchInfoDto> content =
                followRepository.getUserSearchInfoDto(user, followerPage.getContent(), "followee");

        return PaginationResDto.of(followerPage, content);
    }
}
