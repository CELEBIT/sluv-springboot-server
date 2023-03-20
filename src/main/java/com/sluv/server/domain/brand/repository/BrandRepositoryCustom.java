package com.sluv.server.domain.brand.repository;

import com.sluv.server.domain.brand.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandRepositoryCustom {
    Page<Brand> findByAllBrandKrOrBrandEnStartingWith(String brandName, Pageable pageable);
    List<Brand> findTop10By();
}
