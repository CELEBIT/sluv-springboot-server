package com.sluv.server.domain.user.repository.impl;

import static com.sluv.server.domain.user.entity.QFollow.follow;
import static com.sluv.server.domain.user.entity.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Boolean getFollowStatus(User _user, User targetUser) {

        return jpaQueryFactory.selectFrom(user)
                .leftJoin(follow).on(follow.follower.eq(user)).fetchJoin()
                .leftJoin(follow).on(follow.followee.eq(user)).fetchJoin()
                .where(follow.follower.eq(_user)
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
