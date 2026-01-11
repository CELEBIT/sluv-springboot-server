package com.sluv.domain.brand.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.brand.entity.NewBrand;
import com.sluv.domain.brand.entity.RecentSelectBrand;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sluv.domain.brand.entity.QBrand.brand;
import static com.sluv.domain.brand.entity.QNewBrand.newBrand;
import static com.sluv.domain.brand.entity.QRecentSelectBrand.recentSelectBrand;

@RequiredArgsConstructor
public class RecentSelectBrandRepositoryImpl implements RecentSelectBrandRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RecentSelectBrand> getRecentSelectBrandTop20(User user) {
        return jpaQueryFactory.select(recentSelectBrand).distinct()
                .from(recentSelectBrand)
                .leftJoin(recentSelectBrand.brand, brand).fetchJoin()
                .leftJoin(recentSelectBrand.newBrand, newBrand).fetchJoin()
                .where(recentSelectBrand.user.eq(user))
                .groupBy(recentSelectBrand.id, recentSelectBrand.brand, recentSelectBrand.newBrand)
                .limit(20)
                .orderBy(recentSelectBrand.createdAt.max().desc())
                .fetch();
    }

    @Override
    public void changeAllNewBrandToBrand(Brand brand, Long newBrandId) {
        jpaQueryFactory.update(recentSelectBrand)
                .where(recentSelectBrand.newBrand.id.eq(newBrandId))
                .set(recentSelectBrand.brand, brand)
                .set(recentSelectBrand.newBrand, (NewBrand) null)
                .execute();
    }
}
