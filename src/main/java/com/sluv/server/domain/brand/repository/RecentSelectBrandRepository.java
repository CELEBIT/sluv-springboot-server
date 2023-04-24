package com.sluv.server.domain.brand.repository;

import com.sluv.server.domain.brand.entity.RecentSelectBrand;
import com.sluv.server.domain.brand.repository.impl.RecentSelectBrandRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentSelectBrandRepository extends JpaRepository<RecentSelectBrand, Long>, RecentSelectBrandRepositoryCustom {
    void deleteAllByUserId(Long id);
}
