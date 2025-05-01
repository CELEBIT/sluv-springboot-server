package com.sluv.domain.celeb.repository.Impl;

import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.InterestedCeleb;

import java.util.List;

public interface InterestedCelebRepositoryCustom {
    List<InterestedCeleb> findAllByUserId(Long userId);

    void changeAllNewCelebToCeleb(Celeb celeb, Long newCelebId);
}
