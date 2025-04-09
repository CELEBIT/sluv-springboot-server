package com.sluv.domain.user.repository.impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.entity.UserBlock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sluv.domain.user.entity.QUser.user;
import static com.sluv.domain.user.entity.QUserBlock.userBlock;


@RequiredArgsConstructor
public class UserBlockRepositoryImpl implements UserBlockRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean getBlockStatus(User user, User targetUser) {
        List<UserBlock> fetch = jpaQueryFactory.selectFrom(userBlock)
                .where(userBlock.user.eq(user).and(userBlock.blockedUser.eq(targetUser)))
                .fetch();

        return fetch.size() > 0;
    }

    @Override
    public void deleteUserBlock(User user, User targetUser) {
        jpaQueryFactory.delete(userBlock)
                .where(userBlock.user.eq(user).and(userBlock.blockedUser.eq(targetUser)))
                .execute();
    }

    @Override
    public Page<UserBlock> getUserBlockPage(Long userId, Pageable pageable) {
        List<UserBlock> content = jpaQueryFactory.selectFrom(userBlock)
                .where(userBlock.user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<UserBlock> countQuery = jpaQueryFactory.selectFrom(userBlock)
                .where(userBlock.user.id.eq(userId));

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    @Override
    public List<UserBlock> getAllBlockUser(Long userId) {
        return jpaQueryFactory.selectFrom(userBlock)
                .leftJoin(userBlock.blockedUser, user).fetchJoin()
                .where(userBlock.user.id.eq(userId))
                .fetch();
    }
}
