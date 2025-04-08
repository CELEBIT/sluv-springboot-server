package com.sluv.domain.user.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.entity.UserBlock;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
}
