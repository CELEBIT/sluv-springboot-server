package com.sluv.server.domain.user.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import static com.sluv.server.domain.user.entity.QFollow.follow;

@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Boolean getFollowStatus(User user, User writer) {

        return jpaQueryFactory.selectFrom(follow)
                .where(follow.follower.eq(user)
                        .and(follow.followee.eq(writer))
                )
                .fetchFirst() != null;
    }

    @Override
    public void deleteFollow(User user, User targetUser) {
        jpaQueryFactory.delete(follow)
                .where(follow.follower.eq(user)
                        .and(follow.followee.eq(targetUser))
                ).execute();
    }
}
