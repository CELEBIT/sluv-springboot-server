package com.sluv.server.domain.brand.repository;

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
    public List<Brand> findRecentByUserId(User user) {
        return jpaQueryFactory.selectDistinct(recentBrand.brand)
                .from(recentBrand)
                .orderBy(recentBrand.createdAt.desc())
                .limit(10)
                .fetch();
    }
}
