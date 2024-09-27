package com.sluv.domain.search.repository.impl;

import com.sluv.domain.search.entity.RecentSearch;
import com.sluv.domain.user.entity.User;
import java.util.List;

public interface RecentSearchRepositoryCustom {
    List<RecentSearch> getRecentSearch(User user);
}
