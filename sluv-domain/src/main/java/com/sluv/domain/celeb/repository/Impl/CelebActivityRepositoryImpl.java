package com.sluv.domain.celeb.repository.Impl;

import static com.sluv.domain.celeb.entity.QCelebActivity.celebActivity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.celeb.entity.CelebActivity;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CelebActivityRepositoryImpl implements CelebActivityRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CelebActivity> findAllByCelebId(Long celebId) {

        return jpaQueryFactory.selectFrom(celebActivity)
                .where(celebActivity.celeb.id.eq(celebId))
                .limit(8)
                .orderBy(celebActivity.createdAt.desc())
                .fetch();
    }
}
