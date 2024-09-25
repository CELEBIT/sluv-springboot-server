package com.sluv.domain.item.repository.hashtag.impl;

import static com.sluv.domain.item.entity.hashtag.QTempItemHashtag.tempItemHashtag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.entity.hashtag.TempItemHashtag;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TempItemHashtagRepositoryImpl implements TempItemHashtagRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<TempItemHashtag> findAllByTempItems(List<TempItem> tempItems) {
        List<Long> itemItemIds = tempItems.stream().map(TempItem::getId).toList();
        return jpaQueryFactory.selectFrom(tempItemHashtag)
                .where(tempItemHashtag.tempItem.id.in(itemItemIds))
                .fetch();
    }
}
