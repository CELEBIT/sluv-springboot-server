package com.sluv.server.domain.user.repository.impl;

import static com.sluv.server.domain.user.entity.QFollow.follow;
import static com.sluv.server.domain.user.entity.QUser.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.user.dto.UserSearchInfoDto;
import com.sluv.server.domain.user.entity.Follow;
import com.sluv.server.domain.user.entity.User;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Boolean getFollowStatus(User _user, Long targetUserId) {

        return jpaQueryFactory.selectFrom(user)
                .leftJoin(follow).on(follow.follower.eq(user)).fetchJoin()
                .leftJoin(follow).on(follow.followee.eq(user)).fetchJoin()
                .where(follow.follower.eq(_user)
                        .and(follow.followee.id.eq(targetUserId))
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
                .where(follow.followee.eq(targetUser))
                .fetch().size();
    }

    @Override
    public Long getFollowingCount(User targetUser) {
        return (long) jpaQueryFactory.selectFrom(follow)
                .where(follow.follower.eq(targetUser))
                .fetch().size();
    }

    @Override
    public List<UserSearchInfoDto> getUserSearchInfoDto(User nowUser, List<User> content, String target) {
        List<Follow> fetch = jpaQueryFactory.selectFrom(follow)
                .where(getTarget(nowUser, target))
                .fetch();
        Map<Long, List<Follow>> followMap = fetch.stream()
                .collect(Collectors.groupingBy(fol -> getTargetId(fol, target)));

        return content.stream()
                .map(u -> UserSearchInfoDto.of(u, followMap.keySet().contains(u.getId())))
                .toList();
    }

    private static Long getTargetId(Follow fol, String target) {
        Long targetId = fol.getFollowee().getId();
        if (target.equals("followee")) {
            targetId = fol.getFollower().getId();
        }
        return targetId;
    }

    private static BooleanExpression getTarget(User nowUser, String target) {
        BooleanExpression targetUsers = null;
        if (target.equals("follower")) {
            targetUsers = follow.follower.eq(nowUser);
        }
        if (target.equals("followee")) {
            targetUsers = follow.followee.eq(nowUser);
        }
        return targetUsers;
    }
}
