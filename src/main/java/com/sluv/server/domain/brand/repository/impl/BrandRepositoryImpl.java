package com.sluv.server.domain.brand.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.sluv.server.domain.brand.entity.QBrand.brand;
import static com.sluv.server.domain.brand.entity.QRecentBrand.recentBrand;

@RequiredArgsConstructor
public class BrandRepositoryImpl implements BrandRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Brand> findByAllBrandKrOrBrandEnStartingWith(String brandName, Pageable pageable){
        List<Brand> contents = jpaQueryFactory.selectFrom(brand)
                .where(brand.brandKr.like(brandName+"%")
                        .or(brand.brandEn.like(brandName+"%"))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(brand.brandKr.asc())
                .fetch();

        return new PageImpl<>(contents);

    }

    @Override
    public List<Brand> findTop10By() {

        return jpaQueryFactory.select(recentBrand.brand)
                .from(recentBrand)
                .groupBy(recentBrand.brand)
                .orderBy(recentBrand.brand.count().desc())
                .limit(10)
                .fetch();
    }

    @Override
    public Page<Brand> findRecentByUserId(User user, Pageable pageable) {
        List<Brand> content = jpaQueryFactory
                .selectFrom(brand)
                .innerJoin(recentBrand)
                .on(brand.eq(recentBrand.brand))
                .where(recentBrand.user.eq(user))
                .groupBy(brand)
                .orderBy(recentBrand.createdAt.max().desc())
                .limit(10)
                .fetch();

        return new PageImpl<>(content);
    }
}
