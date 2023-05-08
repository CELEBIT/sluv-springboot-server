package com.sluv.server.domain.user.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.sluv.server.domain.user.entity.User;

public interface FollowRepositoryCustom {
    Boolean getFollowStatus(User user, User writer);

    void deleteFollow(User user, User targetUser);
}
