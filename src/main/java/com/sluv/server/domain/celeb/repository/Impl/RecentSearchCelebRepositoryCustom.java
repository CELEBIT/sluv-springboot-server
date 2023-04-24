package com.sluv.server.domain.celeb.repository.Impl;

import com.sluv.server.domain.celeb.entity.RecentSearchCeleb;
import com.sluv.server.domain.user.entity.User;

import java.util.List;

public interface RecentSearchCelebRepositoryCustom {

    List<RecentSearchCeleb> getRecentSearchCelebTop20(User user);
}
