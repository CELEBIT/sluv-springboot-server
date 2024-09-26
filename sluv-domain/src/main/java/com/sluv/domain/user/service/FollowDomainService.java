package com.sluv.domain.user.service;

import com.sluv.domain.user.dto.UserSearchInfoDto;
import com.sluv.domain.user.entity.Follow;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.repository.FollowRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowDomainService {

    private final FollowRepository followRepository;

    @Transactional(readOnly = true)
    public List<Follow> getAllFollower(Long userId) {
        return followRepository.getAllFollower(userId);
    }

    @Transactional(readOnly = true)
    public Boolean getFollowStatus(User user, Long targetUserId) {
        return followRepository.getFollowStatus(user, targetUserId);
    }

    @Transactional
    public void deleteFollow(User user, User targetUser) {
        followRepository.deleteFollow(user, targetUser);
    }

    @Transactional
    public Follow saveFollow(Follow follow) {
        return followRepository.save(follow);
    }

    @Transactional(readOnly = true)
    public List<UserSearchInfoDto> getUserSearchInfoDto(User user, List<User> content, String follower) {
        return followRepository.getUserSearchInfoDto(user, content, follower);
    }

    @Transactional(readOnly = true)
    public Long getFollowerCount(User targetUser) {
        return followRepository.getFollowerCount(targetUser);
    }

    @Transactional(readOnly = true)
    public Long getFollowingCount(User targetUser) {
        return followRepository.getFollowingCount(targetUser);
    }

    @Transactional
    public void deleteFolloweeByUserId(Long userId) {
        followRepository.deleteFolloweeByUserId(userId);
    }

    @Transactional
    public void deleteFollowerByUserId(Long userId) {
        followRepository.deleteFollowerByUserId(userId);
    }
}
