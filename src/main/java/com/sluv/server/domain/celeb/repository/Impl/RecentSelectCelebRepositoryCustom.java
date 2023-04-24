package com.sluv.server.domain.celeb.repository.Impl;

import com.sluv.server.domain.celeb.entity.RecentSelectCeleb;
import com.sluv.server.domain.user.entity.User;

import java.util.List;

public interface RecentSelectCelebRepositoryCustom {

    List<RecentSelectCeleb> getRecentSelectCelebTop20(User user);
}
