package com.sluv.server.domain.brand.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.brand.entity.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.sluv.server.domain.brand.entity.QBrand.brand;

@RequiredArgsConstructor
public class BrandRepositoryImpl implements BrandRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

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

}
