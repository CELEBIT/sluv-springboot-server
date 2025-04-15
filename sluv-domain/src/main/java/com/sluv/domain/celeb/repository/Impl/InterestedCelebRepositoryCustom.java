package com.sluv.domain.celeb.repository.Impl;

import com.sluv.domain.celeb.entity.InterestedCeleb;

import java.util.List;

public interface InterestedCelebRepositoryCustom {
    List<InterestedCeleb> findAllByUserId(Long userId);
}
