package com.sluv.server.domain.search.repository.impl;

import com.sluv.server.domain.search.entity.RecentSearch;
import com.sluv.server.domain.user.entity.User;

import java.util.List;

public interface RecentSearchRepositoryCustom {
    List<RecentSearch> getRecentSearch(User user);
}
