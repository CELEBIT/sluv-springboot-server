package com.sluv.server.domain.brand.repository;

import com.sluv.server.domain.brand.entity.RecentBrand;
import com.sluv.server.domain.brand.repository.impl.RecentBrandRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentBrandRepository extends JpaRepository<RecentBrand, Long>, RecentBrandRepositoryCustom {
}
