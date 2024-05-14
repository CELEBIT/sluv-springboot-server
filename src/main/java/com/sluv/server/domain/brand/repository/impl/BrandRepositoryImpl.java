package com.sluv.server.domain.brand.repository.impl;

import static com.sluv.server.domain.brand.entity.QBrand.brand;
import static com.sluv.server.domain.brand.entity.QRecentSelectBrand.recentSelectBrand;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.brand.entity.Brand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class BrandRepositoryImpl implements BrandRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Brand> findByAllBrandKrOrBrandEnStartingWith(String brandName, Pageable pageable) {
        List<Brand> contents = jpaQueryFactory.selectFrom(brand)
                .where(brand.brandKr.like(brandName + "%")
                        .or(brand.brandEn.like(brandName + "%"))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(brand.brandKr.asc())
                .fetch();

        JPAQuery<Brand> countQuery = jpaQueryFactory.selectFrom(brand)
                .where(brand.brandKr.like(brandName + "%")
                        .or(brand.brandEn.like(brandName + "%"))
                );

        return PageableExecutionUtils.getPage(contents, pageable, () -> countQuery.fetch().size());
    }

    @Override
    public List<Brand> findTop10By() {

        return jpaQueryFactory.select(recentSelectBrand.brand)
                .from(recentSelectBrand)
                .groupBy(recentSelectBrand.brand)
                .orderBy(recentSelectBrand.brand.count().desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<Brand> getBrandContainKeyword(String keyword) {
        return jpaQueryFactory.selectFrom(brand)
                .where(brand.brandKr.like("%" + keyword + "%")
                        .or(brand.brandKr.like("%" + keyword + "%"))
                )
                .limit(5)
                .fetch();
    }
}
