package com.sluv.server.domain.brand.repository.impl;

import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandRepositoryCustom {
    Page<Brand> findByAllBrandKrOrBrandEnStartingWith(String brandName, Pageable pageable);
    List<Brand> findTop10By();
    Page<Brand> findRecentByUserId(User user, Pageable pageable);
}
