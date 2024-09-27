package com.sluv.domain.item.repository.hashtag.impl;

import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.entity.hashtag.TempItemHashtag;
import java.util.List;

public interface TempItemHashtagRepositoryCustom {
    List<TempItemHashtag> findAllByTempItems(List<TempItem> tempItems);
}
