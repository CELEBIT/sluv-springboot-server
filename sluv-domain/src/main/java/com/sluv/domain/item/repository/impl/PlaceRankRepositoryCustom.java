package com.sluv.domain.item.repository.impl;

import com.sluv.domain.item.entity.PlaceRank;
import java.util.List;

public interface PlaceRankRepositoryCustom {
    List<PlaceRank> getRecentPlaceTop20(Long userId);
}
