package com.sluv.domain.celeb.repository.Impl;

import com.sluv.domain.celeb.entity.CelebActivity;
import java.util.List;

public interface CelebActivityRepositoryCustom {
    List<CelebActivity> findAllByCelebId(Long celebId);
}
