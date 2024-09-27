package com.sluv.domain.user.service;

import com.sluv.domain.user.dto.UserSearchInfoDto;
import com.sluv.domain.user.entity.Follow;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowDomainService {

    private final FollowRepository followRepository;

    public List<Follow> getAllFollower(Long userId) {
        return followRepository.getAllFollower(userId);
    }

    public Boolean getFollowStatus(User user, Long targetUserId) {
        return followRepository.getFollowStatus(user, targetUserId);
    }

    public void deleteFollow(User user, User targetUser) {
        followRepository.deleteFollow(user, targetUser);
    }

    public Follow saveFollow(Follow follow) {
        return followRepository.save(follow);
    }

    public List<UserSearchInfoDto> getUserSearchInfoDto(User user, List<User> content, String follower) {
        return followRepository.getUserSearchInfoDto(user, content, follower);
    }

    public Long getFollowerCount(User targetUser) {
        return followRepository.getFollowerCount(targetUser);
    }

    public Long getFollowingCount(User targetUser) {
        return followRepository.getFollowingCount(targetUser);
    }

    public void deleteFolloweeByUserId(Long userId) {
        followRepository.deleteFolloweeByUserId(userId);
    }

    public void deleteFollowerByUserId(Long userId) {
        followRepository.deleteFollowerByUserId(userId);
    }
}
