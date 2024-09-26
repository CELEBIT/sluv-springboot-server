package com.sluv.domain.item.repository.impl;

import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.item.entity.Item;
import java.util.List;

public interface ItemScrapRepositoryCustom {
    Boolean getItemScrapStatus(Item item, List<Closet> closetList);
}
