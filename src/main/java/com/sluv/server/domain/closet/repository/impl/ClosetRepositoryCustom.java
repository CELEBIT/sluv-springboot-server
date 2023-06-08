package com.sluv.server.domain.closet.repository.impl;

import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.item.entity.Item;

import java.util.List;

public interface ClosetRepositoryCustom {
    List<Closet> getRecentAddCloset(Item item);
}
