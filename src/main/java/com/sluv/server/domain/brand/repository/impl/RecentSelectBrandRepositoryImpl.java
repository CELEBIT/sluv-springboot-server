package com.sluv.server.domain.brand.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.brand.entity.RecentSelectBrand;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sluv.server.domain.brand.entity.QRecentBrand.recentBrand;

@RequiredArgsConstructor
public class RecentSelectBrandRepositoryImpl implements RecentSelectBrandRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RecentSelectBrand> getRecentSearchBrandTop20(User user) {
        return jpaQueryFactory.selectFrom(recentBrand)
                .where(recentBrand.user.eq(user))
                .groupBy(recentBrand.brand, recentBrand.newBrand)
                .limit(20)
                .orderBy(recentBrand.createdAt.max().desc())
                .fetch();
    }
}
