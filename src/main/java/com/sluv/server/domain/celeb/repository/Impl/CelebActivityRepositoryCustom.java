package com.sluv.server.domain.celeb.repository.Impl;

import com.sluv.server.domain.celeb.entity.CelebActivity;

import java.util.List;

public interface CelebActivityRepositoryCustom {
    List<CelebActivity> findAllByCelebId(Long celebId);
}
