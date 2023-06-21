package com.sluv.server.domain.user.repository.impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.user.entity.QUser;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sluv.server.domain.user.entity.QFollow.follow;
import static com.sluv.server.domain.user.entity.QUser.user;
import static com.sluv.server.domain.user.enums.UserStatus.ACTIVE;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<User> getSearchUser(List<Long> userIdList, Pageable pageable) {
        List<User> content = jpaQueryFactory.selectFrom(user)
                .where(user.id.in(userIdList)
                        .and(user.userStatus.eq(ACTIVE))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(user.createdAt.desc()) // 추후 예정
                .fetch();

        // Count Query
        JPAQuery<User> countJPAQuery = jpaQueryFactory.selectFrom(user)
                .where(user.id.in(userIdList)
                        .and(user.userStatus.eq(ACTIVE))
                )
                .orderBy(user.createdAt.desc());// 추후 예정


        return PageableExecutionUtils.getPage(content, pageable, () -> countJPAQuery.fetch().size());
    }

    /**
     * 특정 유저의 팔로워 조회
     *
     */
    @Override
    public Page<User> getAllFollower(Long userId, Pageable pageable) {
        List<User> content = jpaQueryFactory.select(user)
                .from(follow)
                .leftJoin(follow.follower, user)
                .where(follow.followee.id.eq(userId).and(follow.followee.userStatus.eq(ACTIVE)))
                .orderBy(follow.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<User> query = jpaQueryFactory.select(user)
                .from(follow)
                .leftJoin(follow.follower, user)
                .where(follow.followee.id.eq(userId).and(follow.followee.userStatus.eq(ACTIVE)))
                .orderBy(follow.id.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }

    /**
     * 특정 유저의 팔로잉 조회
     *
     */
    @Override
    public Page<User> getAllFollowing(Long userId, Pageable pageable) {
        List<User> content = jpaQueryFactory.select(user)
                .from(follow)
                .leftJoin(follow.followee, user)
                .where(follow.follower.id.eq(userId).and(follow.follower.userStatus.eq(ACTIVE)))
                .orderBy(follow.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<User> query = jpaQueryFactory.select(user)
                .from(follow)
                .leftJoin(follow.followee, user)
                .where(follow.follower.id.eq(userId).and(follow.follower.userStatus.eq(ACTIVE)))
                .orderBy(follow.id.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }
}
