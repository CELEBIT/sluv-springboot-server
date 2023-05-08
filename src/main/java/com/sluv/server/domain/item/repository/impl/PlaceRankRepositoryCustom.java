package com.sluv.server.domain.item.repository.impl;

import com.sluv.server.domain.item.entity.PlaceRank;
import com.sluv.server.domain.user.entity.User;

import java.util.List;

public interface PlaceRankRepositoryCustom {
    List<PlaceRank> getRecentPlaceTop20(User user);
}
