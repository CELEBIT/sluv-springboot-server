package com.sluv.server.domain.item.repository.impl;

import com.sluv.server.domain.item.entity.Item;

import java.util.List;

public interface ItemRepositoryCustom {
    List<String> findTopPlace();
    List<Item> findSameCelebItem(Item item, boolean celebJudge);

    List<Item> findSameBrandItem(Item item, boolean brandJudge);
}
