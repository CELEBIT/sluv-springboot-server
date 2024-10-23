package com.sluv.domain.brand.repository.impl;

import com.sluv.domain.brand.dto.BrandCountDto;
import com.sluv.domain.brand.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandRepositoryCustom {
    Page<Brand> findByAllBrandKrOrBrandEnStartingWith(String brandName, Pageable pageable);

    List<Brand> findTop10By();

    List<Brand> getBrandContainKeyword(String keyword);

    List<BrandCountDto> getTopHotBrandWithLimit(int limitCount);

    Page<Brand> findAllWithPageable(Pageable pageable);
}
