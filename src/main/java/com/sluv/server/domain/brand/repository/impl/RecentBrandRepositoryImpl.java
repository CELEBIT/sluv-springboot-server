package com.sluv.server.domain.brand.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.entity.RecentBrand;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.sluv.server.domain.brand.entity.QBrand.brand;
import static com.sluv.server.domain.brand.entity.QRecentBrand.recentBrand;

@RequiredArgsConstructor
public class RecentBrandRepositoryImpl implements RecentBrandRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RecentBrand> getRecentSearchBrandTop20(User user) {
        return jpaQueryFactory.selectFrom(recentBrand)
                .where(recentBrand.user.eq(user))
                .groupBy(recentBrand.brand, recentBrand.newBrand)
                .limit(20)
                .orderBy(recentBrand.createdAt.max().desc())
                .fetch();
    }
}
