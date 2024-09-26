package com.sluv.domain.item.repository.impl;

import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.entity.TempItemImg;
import java.util.List;

public interface TempItemImgRepositoryCustom {
    List<TempItemImg> findAllByTempItems(List<TempItem> tempItems);
}
