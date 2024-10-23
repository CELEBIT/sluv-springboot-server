package com.sluv.domain.brand.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.brand.dto.BrandCountDto;
import com.sluv.domain.brand.entity.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sluv.domain.brand.entity.QBrand.brand;
import static com.sluv.domain.brand.entity.QRecentSelectBrand.recentSelectBrand;
import static com.sluv.domain.item.entity.QItem.item;

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

    @Override
    public List<BrandCountDto> getTopHotBrandWithLimit(int limitCount) {
        List<Tuple> fetch = jpaQueryFactory.select(brand, item.brand.count())
                .from(item)
                .groupBy(item.brand)
                .orderBy(item.brand.count().desc())
                .limit(limitCount)
                .fetch();

        return fetch.stream()
                .map(tuple -> BrandCountDto.of(tuple.get(brand), tuple.get(item.brand.count())))
                .toList();
    }

    @Override
    public Page<Brand> findAllWithPageable(Pageable pageable) {
        List<Brand> content = jpaQueryFactory.selectFrom(brand)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //CountQuery
        JPAQuery<Brand> countQuery = jpaQueryFactory.selectFrom(brand);

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }
}
