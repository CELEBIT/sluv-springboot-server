package com.sluv.domain.user.repository.impl;

import com.sluv.domain.user.dto.UserSearchInfoDto;
import com.sluv.domain.user.entity.Follow;
import com.sluv.domain.user.entity.User;
import java.util.List;

public interface FollowRepositoryCustom {
    Boolean getFollowStatus(User user, Long targetUserId);

    void deleteFollow(User user, User targetUser);

    Long getFollowerCount(User targetUser);

    Long getFollowingCount(User targetUser);

    List<UserSearchInfoDto> getUserSearchInfoDto(User nowUser, List<User> content, String target);

    void deleteFolloweeByUserId(Long userId);

    void deleteFollowerByUserId(Long userId);

    List<Follow> getAllFollower(Long userId);
}
