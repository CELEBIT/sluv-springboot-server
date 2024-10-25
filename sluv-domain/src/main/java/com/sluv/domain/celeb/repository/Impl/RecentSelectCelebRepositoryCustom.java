package com.sluv.domain.celeb.repository.Impl;

import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.RecentSelectCeleb;
import com.sluv.domain.user.entity.User;

import java.util.List;

public interface RecentSelectCelebRepositoryCustom {

    List<RecentSelectCeleb> getRecentSelectCelebTop20(User user);

    void changeAllNewCelebToCeleb(Celeb celeb, Long newCelebId);
}
