package com.sluv.server.domain.celeb.repository.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.celeb.entity.RecentSelectCeleb;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sluv.server.domain.celeb.entity.QRecentSelectCeleb.recentSelectCeleb;

@RequiredArgsConstructor
public class RecentSelectCelebRepositoryImpl implements RecentSelectCelebRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RecentSelectCeleb> getRecentSearchCelebTop20(User user) {
        return jpaQueryFactory.selectFrom(recentSelectCeleb)
                .where(recentSelectCeleb.user.eq(user))
                .limit(20)
                .groupBy(recentSelectCeleb.celeb, recentSelectCeleb.newCeleb)
                .orderBy(recentSelectCeleb.createdAt.max().desc())
                .fetch();
    }
}
