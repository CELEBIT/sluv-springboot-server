package com.sluv.api.user.service;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.domain.user.dto.UserSearchInfoDto;
import com.sluv.domain.user.entity.Follow;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.FollowDomainService;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.infra.alarm.service.UserAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserDomainService userDomainService;
    private final FollowDomainService followDomainService;
    private final UserAlarmService userAlarmService;

    @Transactional
    public void postUserFollow(Long userId, Long targetId) {
        User user = userDomainService.findById(userId);
        // target이 될 유저 검색
        User targetUser = userDomainService.findById(targetId);

        // Follow 여부 확인.
        Boolean followStatus = followDomainService.getFollowStatus(user, targetUser.getId());

        if (followStatus) {
            followDomainService.deleteFollow(user, targetUser);
        } else {
            // Follow 정보 등록.
            followDomainService.saveFollow(Follow.toEntity(user, targetUser));
            userAlarmService.sendAlarmAboutFollow(user.getId(), targetUser.getId());
        }

    }

    @Transactional(readOnly = true)
    public PaginationResponse<UserSearchInfoDto> getUserFollower(Long userId, Long targetId, Pageable pageable) {
        User user = userDomainService.findById(userId);
        // Follower 들 조회
        Page<User> followerPage = userDomainService.getAllFollower(targetId, pageable);

        // UserSearchInfoDto로 가공
        List<UserSearchInfoDto> content =
                followDomainService.getUserSearchInfoDto(user, followerPage.getContent(), "follower");

        return PaginationResponse.create(followerPage, content);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<UserSearchInfoDto> getUserFollowing(Long userId, Long targetId, Pageable pageable) {
        User user = userDomainService.findById(userId);

        // Following 들 조회
        Page<User> followerPage = userDomainService.getAllFollowing(targetId, pageable);

        // UserSearchInfoDto로 가공
        List<UserSearchInfoDto> content =
                followDomainService.getUserSearchInfoDto(user, followerPage.getContent(), "followee");

        return PaginationResponse.create(followerPage, content);
    }

}
