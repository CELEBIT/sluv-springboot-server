package com.sluv.server.domain.item.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.item.entity.Item;

import java.util.List;

public interface ItemScrapRepositoryCustom {
    Boolean getItemScrapStatus(Item item, List<Closet> closetList);
}
