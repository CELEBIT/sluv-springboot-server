package com.sluv.server.domain.user.repository.impl;

import static com.sluv.server.domain.user.entity.QFollow.follow;
import static com.sluv.server.domain.user.entity.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.user.dto.UserSearchInfoDto;
import com.sluv.server.domain.user.entity.Follow;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.enums.UserStatus;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Boolean getFollowStatus(User _user, Long targetUserId) {

        if (_user == null) {
            return false;
        }

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
                .where(follow.followee.eq(targetUser)
                        .and(follow.follower.userStatus.eq(UserStatus.ACTIVE)))
                .fetch().size();
    }

    @Override
    public Long getFollowingCount(User targetUser) {
        return (long) jpaQueryFactory.selectFrom(follow)
                .where(follow.follower.eq(targetUser)
                        .and(follow.followee.userStatus.eq(UserStatus.ACTIVE)))
                .fetch().size();
    }

    @Override
    public List<UserSearchInfoDto> getUserSearchInfoDto(User nowUser, List<User> content, String target) {
        List<Follow> fetch = jpaQueryFactory.selectFrom(follow)
                .where(follow.follower.eq(nowUser))
                .fetch();

        Map<Long, List<Follow>> followMap = fetch.stream()
                .collect(Collectors.groupingBy(fol -> fol.getFollowee().getId()));

        return content.stream()
                .map(u -> UserSearchInfoDto.of(u, followMap.containsKey(u.getId()),
                        Objects.equals(u.getId(), nowUser.getId())))
                .toList();
    }

    @Override
    public void deleteFolloweeByUserId(Long userId) {
        jpaQueryFactory.delete(follow)
                .where(follow.followee.id.eq(userId))
                .execute();
    }

    @Override
    public void deleteFollowerByUserId(Long userId) {
        jpaQueryFactory.delete(follow)
                .where(follow.follower.id.eq(userId))
                .execute();
    }

    @Override
    public List<Follow> getAllFollower(Long userId) {
        return jpaQueryFactory.selectFrom(follow)
                .where(follow.followee.id.eq(userId))
                .fetch();
    }
}
