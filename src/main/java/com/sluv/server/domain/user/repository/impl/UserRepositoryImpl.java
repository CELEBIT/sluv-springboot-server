package com.sluv.server.domain.user.repository.impl;

import static com.sluv.server.domain.celeb.entity.QInterestedCeleb.interestedCeleb;
import static com.sluv.server.domain.item.entity.QItem.item;
import static com.sluv.server.domain.item.entity.QItemLike.itemLike;
import static com.sluv.server.domain.user.entity.QFollow.follow;
import static com.sluv.server.domain.user.entity.QUser.user;
import static com.sluv.server.domain.user.enums.UserStatus.ACTIVE;
import static com.sluv.server.domain.user.enums.UserStatus.DELETED;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.item.enums.ItemStatus;
import com.sluv.server.domain.user.entity.Follow;
import com.sluv.server.domain.user.entity.User;
import java.time.LocalDateTime;
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
    public List<User> getHotSluver(Long celebId) {
        LocalDateTime now = LocalDateTime.now();

        JPAQuery<User> query = jpaQueryFactory.selectFrom(user)
                .leftJoin(follow).on(follow.followee.eq(user))
                .leftJoin(item).on(item.user.eq(user))
                .leftJoin(itemLike).on(itemLike.item.eq(item))
//                .leftJoin(question).on(question.user.eq(user))
//                .leftJoin(questionLike).on(questionLike.question.eq(question))
//                .leftJoin(comment).on(comment.user.eq(user))
//                .leftJoin(commentLike).on(commentLike.comment.eq(comment))
                .where(user.userStatus.eq(ACTIVE)
                                .or(follow.createdAt.goe(now.minusDays(31)))
                                .or(item.itemStatus.eq(ItemStatus.ACTIVE)
                                        .and(item.createdAt.goe(now.minusDays(31)))
                                )
//                        .or(question.questionStatus.eq(QuestionStatus.ACTIVE)
//                                .and(question.createdAt.goe(now.minusDays(31)))
//                        )
//                        .or(comment.commentStatus.eq(CommentStatus.ACTIVE)
//                                .and(comment.createdAt.goe(now.minusDays(31)))
//                        )

                )
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
                                .add(itemLike.count())
//                                .add(questionLike.count())
//                                .add(commentLike.count())
                                .desc()
                )
                .limit(10)
                .fetch();
    }

    @Override
    public List<User> getSearchUserIds(String word) {
        return jpaQueryFactory.selectFrom(user)
                .where(user.nickname.like("%" + word + "%"))
                .fetch();
    }

    @Override
    public long getNotDeleteUserCount() {
        List<User> users = jpaQueryFactory.selectFrom(user)
                .where(user.userStatus.ne(DELETED))
                .fetch();
        return users.stream().count();
    }

    @Override
    public List<User> getDeletedUsersAfter7Days() {
        return jpaQueryFactory.selectFrom(user)
                .where(user.userStatus.eq(DELETED)
                        .and(user.updatedAt.before(LocalDateTime.now().minusDays(7)))
                ).fetch();
    }
}
