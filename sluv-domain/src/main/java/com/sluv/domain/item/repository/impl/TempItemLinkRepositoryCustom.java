package com.sluv.domain.item.repository.impl;

import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.entity.TempItemLink;
import java.util.List;

public interface TempItemLinkRepositoryCustom {
    List<TempItemLink> findAllByTempItems(List<TempItem> tempItems);
}
