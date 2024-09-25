package com.sluv.domain.brand.repository;

import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.brand.repository.impl.BrandRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long>, BrandRepositoryCustom {
}
