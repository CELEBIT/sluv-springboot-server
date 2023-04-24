package com.sluv.server.domain.brand.repository.impl;

import com.sluv.server.domain.brand.entity.RecentBrand;
import com.sluv.server.domain.user.entity.User;

import java.util.List;

public interface RecentBrandRepositoryCustom {
    List<RecentBrand> getRecentSearchBrandTop20(User user);
}
