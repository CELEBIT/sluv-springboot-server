package com.sluv.domain.brand.repository.impl;

import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.brand.entity.RecentSelectBrand;
import com.sluv.domain.user.entity.User;

import java.util.List;

public interface RecentSelectBrandRepositoryCustom {
    List<RecentSelectBrand> getRecentSelectBrandTop20(User user);

    void changeAllNewBrandToBrand(Brand brand, Long newBrandId);
}
