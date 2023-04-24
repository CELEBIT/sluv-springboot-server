package com.sluv.server.domain.brand.repository.impl;

import com.sluv.server.domain.brand.entity.RecentSelectBrand;
import com.sluv.server.domain.user.entity.User;

import java.util.List;

public interface RecentSelectBrandRepositoryCustom {
    List<RecentSelectBrand> getRecentSearchBrandTop20(User user);
}
