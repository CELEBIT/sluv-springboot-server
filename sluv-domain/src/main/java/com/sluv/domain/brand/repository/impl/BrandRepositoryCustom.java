package com.sluv.domain.brand.repository.impl;

import com.sluv.domain.brand.entity.Brand;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BrandRepositoryCustom {
    Page<Brand> findByAllBrandKrOrBrandEnStartingWith(String brandName, Pageable pageable);

    List<Brand> findTop10By();

    List<Brand> getBrandContainKeyword(String keyword);
}
