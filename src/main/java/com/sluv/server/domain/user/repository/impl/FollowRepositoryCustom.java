package com.sluv.server.domain.user.repository.impl;

import com.sluv.server.domain.user.dto.UserSearchInfoDto;
import com.sluv.server.domain.user.entity.User;
import java.util.List;

public interface FollowRepositoryCustom {
    Boolean getFollowStatus(User user, Long targetUserId);

    void deleteFollow(User user, User targetUser);

    Long getFollowerCount(User targetUser);

    Long getFollowingCount(User targetUser);

    List<UserSearchInfoDto> getUserSearchInfoDto(User nowUser, List<User> content, String target);
}
