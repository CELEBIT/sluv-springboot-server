package com.sluv.server.domain.item.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.item.entity.ItemImg;
import lombok.RequiredArgsConstructor;

import static com.sluv.server.domain.item.entity.QItemImg.itemImg;

@RequiredArgsConstructor
public class ItemImgRepositoryImpl implements ItemImgRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public ItemImg findMainImg(Long itemId) {
        return jpaQueryFactory.selectFrom(itemImg)
                .where(itemImg.item.id.eq(itemId)
                        .and(itemImg.representFlag.eq(true))
                )
                .fetchOne();
    }
}
