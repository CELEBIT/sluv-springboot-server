package com.sluv.domain.brand.repository;

import com.sluv.domain.brand.entity.RecentSelectBrand;
import com.sluv.domain.brand.repository.impl.RecentSelectBrandRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentSelectBrandRepository extends JpaRepository<RecentSelectBrand, Long>,
        RecentSelectBrandRepositoryCustom {
    void deleteAllByUserId(Long id);

    void deleteByUserIdAndBrandId(Long userId, Long brandId);

    void deleteByUserIdAndNewBrandId(Long userId, Long newBrandId);
}
