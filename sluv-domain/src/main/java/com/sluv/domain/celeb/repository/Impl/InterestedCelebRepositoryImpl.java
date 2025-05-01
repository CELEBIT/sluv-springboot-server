package com.sluv.domain.celeb.repository.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.InterestedCeleb;
import com.sluv.domain.celeb.entity.NewCeleb;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sluv.domain.celeb.entity.QCeleb.celeb;
import static com.sluv.domain.celeb.entity.QInterestedCeleb.interestedCeleb;
import static com.sluv.domain.celeb.entity.QNewCeleb.newCeleb;

@RequiredArgsConstructor
public class InterestedCelebRepositoryImpl implements InterestedCelebRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<InterestedCeleb> findAllByUserId(Long userId) {
        return jpaQueryFactory.selectFrom(interestedCeleb)
                .leftJoin(interestedCeleb.celeb, celeb).fetchJoin()
                .leftJoin(interestedCeleb.newCeleb, newCeleb).fetchJoin()
                .where(interestedCeleb.user.id.eq(userId))
                .fetch();
    }

    @Override
    public void changeAllNewCelebToCeleb(Celeb celeb, Long newCelebId) {
        jpaQueryFactory.update(interestedCeleb)
                .set(interestedCeleb.newCeleb, (NewCeleb) null)
                .set(interestedCeleb.celeb, celeb)
                .where(interestedCeleb.newCeleb.id.eq(newCelebId))
                .execute();
    }
}
