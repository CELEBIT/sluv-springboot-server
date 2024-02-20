package com.sluv.server.domain.user.repository.impl;

import static com.sluv.server.domain.celeb.entity.QInterestedCeleb.interestedCeleb;
import static com.sluv.server.domain.item.entity.QItem.item;
import static com.sluv.server.domain.user.entity.QFollow.follow;
import static com.sluv.server.domain.user.entity.QUser.user;
import static com.sluv.server.domain.user.enums.UserStatus.ACTIVE;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.user.entity.Follow;
import com.sluv.server.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
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
     */
    @Override
    public Page<User> getAllFollower(Long userId, Pageable pageable) {
        List<User> content = jpaQueryFactory.select(user)
                .from(follow)
                .leftJoin(user).on(follow.follower.eq(user))
                .where(follow.followee.id.eq(userId).and(follow.follower.userStatus.eq(ACTIVE)))
                .orderBy(follow.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<Follow> query = jpaQueryFactory.selectFrom(follow)
                .where(follow.followee.id.eq(userId).and(follow.follower.userStatus.eq(ACTIVE)))
                .orderBy(follow.id.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }

    /**
     * 특정 유저가 팔로잉하고 있는 유저 조회
     */
    @Override
    public Page<User> getAllFollowing(Long userId, Pageable pageable) {
        List<User> content = jpaQueryFactory.select(user)
                .from(follow)
                .leftJoin(user).on(follow.followee.eq(user))
                .where(follow.follower.id.eq(userId).and(follow.followee.userStatus.eq(ACTIVE)))
                .orderBy(follow.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<Follow> query = jpaQueryFactory.selectFrom(follow)
                .where(follow.follower.id.eq(userId).and(follow.followee.userStatus.eq(ACTIVE)))
                .orderBy(follow.id.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }

    /**
     * 전체 / 셀럽 별 인기 스러버 조회
     */
    @Override
    public List<User> getHotSluver(User _user, Long celebId) {

        JPAQuery<User> query = jpaQueryFactory.selectFrom(user)
                .leftJoin(follow).on(follow.followee.eq(user)).fetchJoin()
                .leftJoin(item).on(item.user.eq(user)).fetchJoin()
//                .leftJoin(itemLike).on(itemLike.item.user.eq(user)).fetchJoin()
//                .leftJoin(questionLike).on(questionLike.question.user.eq(user)).fetchJoin()
//                .leftJoin(commentLike).on(commentLike.comment.user.eq(user)).fetchJoin()
                .where(user.userStatus.eq(ACTIVE))
                .groupBy(user);

        if (celebId != null) {
            List<User> userList;
            userList = jpaQueryFactory.selectFrom(user)
                    .leftJoin(interestedCeleb).on(interestedCeleb.user.eq(user)).fetchJoin()
                    .where(interestedCeleb.celeb.id.eq(celebId))
                    .fetch();

            query.where(user.in(userList));
        }

        return query
                .orderBy(
                        follow.count()
                                .add(item.count())
//                                .add(itemLike.count())
//                                .add(questionLike.count())
//                                .add(commentLike.count())
                                .desc()
                )
                .limit(10)
                .fetch();
    }
}
