package com.sluv.server.domain.brand.repository;

import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.repository.impl.BrandRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long>, BrandRepositoryCustom {
}
