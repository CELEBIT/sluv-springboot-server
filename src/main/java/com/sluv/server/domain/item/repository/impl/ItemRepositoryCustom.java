package com.sluv.server.domain.item.repository.impl;

import com.sluv.server.domain.item.dto.HotPlaceResDto;

import java.util.List;

public interface ItemRepositoryCustom {
    List<String> findTopPlace();
}
