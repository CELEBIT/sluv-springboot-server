package com.sluv.domain.celeb.repository.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.celeb.entity.QCeleb;
import com.sluv.domain.celeb.entity.RecentSelectCeleb;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sluv.domain.celeb.entity.QCeleb.celeb;
import static com.sluv.domain.celeb.entity.QNewCeleb.newCeleb;
import static com.sluv.domain.celeb.entity.QRecentSelectCeleb.recentSelectCeleb;

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

    @Override
    public void changeAllNewCelebToCeleb(Celeb celeb, Long newCelebId) {
        jpaQueryFactory.update(recentSelectCeleb)
                .where(recentSelectCeleb.newCeleb.id.eq(newCelebId))
                .set(recentSelectCeleb.celeb, celeb)
                .set(recentSelectCeleb.newCeleb, (NewCeleb) null)
                .execute();
    }

}
