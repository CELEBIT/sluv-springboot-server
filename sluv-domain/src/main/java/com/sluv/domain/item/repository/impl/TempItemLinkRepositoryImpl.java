package com.sluv.domain.item.repository.impl;

import static com.sluv.domain.item.entity.QTempItemLink.tempItemLink;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.entity.TempItemLink;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TempItemLinkRepositoryImpl implements TempItemLinkRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<TempItemLink> findAllByTempItems(List<TempItem> tempItems) {
        List<Long> tempItemIds = tempItems.stream().map(TempItem::getId).toList();
        return jpaQueryFactory.selectFrom(tempItemLink)
                .where(tempItemLink.tempItem.id.in(tempItemIds))
                .fetch();
    }
}
