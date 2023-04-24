package com.sluv.server.domain.celeb.repository.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.celeb.entity.RecentSearchCeleb;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sluv.server.domain.celeb.entity.QRecentSearchCeleb.recentSearchCeleb;

@RequiredArgsConstructor
public class RecentSearchCelebRepositoryImpl implements RecentSearchCelebRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RecentSearchCeleb> getRecentSearchCelebTop20(User user) {
        return jpaQueryFactory.selectFrom(recentSearchCeleb)
                .where(recentSearchCeleb.user.eq(user))
                .limit(20)
                .groupBy(recentSearchCeleb.celeb, recentSearchCeleb.newCeleb)
                .orderBy(recentSearchCeleb.createdAt.max().desc())
                .fetch();
    }
}
