package com.sluv.server.domain.user.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import static com.sluv.server.domain.user.entity.QFollow.follow;

@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Boolean getFollowStatus(User user, User targetUser) {

        return jpaQueryFactory.selectFrom(follow)
                .where(follow.follower.eq(user)
                        .and(follow.followee.eq(targetUser))
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

    @Override
    public Long getFollowerCount(User targetUser) {
        return (long) jpaQueryFactory.selectFrom(follow)
                .where(follow.follower.eq(targetUser))
                .fetch().size();
    }

    @Override
    public Long getFollowingCount(User targetUser) {
        return (long) jpaQueryFactory.selectFrom(follow)
                .where(follow.followee.eq(targetUser))
                .fetch().size();
    }
}
