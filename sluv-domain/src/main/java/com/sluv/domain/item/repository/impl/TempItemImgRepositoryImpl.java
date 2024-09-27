package com.sluv.domain.item.repository.impl;

import static com.sluv.domain.item.entity.QTempItemImg.tempItemImg;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.entity.TempItemImg;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TempItemImgRepositoryImpl implements TempItemImgRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<TempItemImg> findAllByTempItems(List<TempItem> tempItems) {
        List<Long> itemItemIds = tempItems.stream().map(TempItem::getId).toList();
        return jpaQueryFactory.selectFrom(tempItemImg)
                .where(tempItemImg.tempItem.id.in(itemItemIds))
                .fetch();
    }

}
