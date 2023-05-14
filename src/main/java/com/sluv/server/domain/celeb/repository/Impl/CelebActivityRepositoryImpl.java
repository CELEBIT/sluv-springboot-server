package com.sluv.server.domain.celeb.repository.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.celeb.entity.CelebActivity;
import com.sluv.server.domain.celeb.entity.QCelebActivity;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sluv.server.domain.celeb.entity.QCelebActivity.celebActivity;

@RequiredArgsConstructor
public class CelebActivityRepositoryImpl implements CelebActivityRepositoryCustom{
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
