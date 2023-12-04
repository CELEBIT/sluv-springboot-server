package com.sluv.server.domain.brand.repository.impl;

import static com.sluv.server.domain.brand.entity.QBrand.brand;
import static com.sluv.server.domain.brand.entity.QNewBrand.newBrand;
import static com.sluv.server.domain.brand.entity.QRecentSelectBrand.recentSelectBrand;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.brand.entity.RecentSelectBrand;
import com.sluv.server.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecentSelectBrandRepositoryImpl implements RecentSelectBrandRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RecentSelectBrand> getRecentSelectBrandTop20(User user) {
        return jpaQueryFactory.selectFrom(recentSelectBrand)
                .leftJoin(recentSelectBrand.brand, brand).fetchJoin()
                .leftJoin(recentSelectBrand.newBrand, newBrand).fetchJoin()
                .where(recentSelectBrand.user.eq(user))
                .groupBy(recentSelectBrand.brand, recentSelectBrand.newBrand)
                .limit(20)
                .orderBy(recentSelectBrand.createdAt.max().desc())
                .fetch();
    }
}
