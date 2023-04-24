package com.sluv.server.domain.brand.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.brand.entity.RecentSelectBrand;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sluv.server.domain.brand.entity.QRecentSelectBrand.recentSelectBrand;

@RequiredArgsConstructor
public class RecentSelectBrandRepositoryImpl implements RecentSelectBrandRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RecentSelectBrand> getRecentSelectBrandTop20(User user) {
        return jpaQueryFactory.selectFrom(recentSelectBrand)
                .where(recentSelectBrand.user.eq(user))
                .groupBy(recentSelectBrand.brand, recentSelectBrand.newBrand)
                .limit(20)
                .orderBy(recentSelectBrand.createdAt.max().desc())
                .fetch();
    }
}
