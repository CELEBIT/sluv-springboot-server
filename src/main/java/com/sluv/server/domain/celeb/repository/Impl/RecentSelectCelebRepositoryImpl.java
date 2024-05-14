package com.sluv.server.domain.celeb.repository.Impl;

import static com.sluv.server.domain.celeb.entity.QCeleb.celeb;
import static com.sluv.server.domain.celeb.entity.QNewCeleb.newCeleb;
import static com.sluv.server.domain.celeb.entity.QRecentSelectCeleb.recentSelectCeleb;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.celeb.entity.QCeleb;
import com.sluv.server.domain.celeb.entity.RecentSelectCeleb;
import com.sluv.server.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecentSelectCelebRepositoryImpl implements RecentSelectCelebRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RecentSelectCeleb> getRecentSelectCelebTop20(User user) {
        QCeleb parent = new QCeleb("parent");

        return jpaQueryFactory.selectFrom(recentSelectCeleb)
                .leftJoin(recentSelectCeleb.celeb, celeb).fetchJoin()
                .leftJoin(celeb.parent, parent).fetchJoin()
                .leftJoin(recentSelectCeleb.newCeleb, newCeleb).fetchJoin()
                .where(recentSelectCeleb.user.eq(user))
                .limit(20)
                .groupBy(recentSelectCeleb.celeb, recentSelectCeleb.newCeleb)
                .orderBy(recentSelectCeleb.createdAt.max().desc())
                .fetch();
    }
}
