package com.sluv.server.domain.brand.repository;

import com.sluv.server.domain.brand.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BrandRepositoryCustom {
    public Page<Brand> findByAllBrandKrOrBrandEnStartingWith(String brandName, Pageable pageable);
}
