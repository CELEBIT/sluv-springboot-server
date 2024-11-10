package com.sluv.domain.item.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.item.entity.ItemEditReq;
import lombok.RequiredArgsConstructor;

import static com.sluv.domain.item.entity.QItem.item;
import static com.sluv.domain.item.entity.QItemEditReq.itemEditReq;

@RequiredArgsConstructor
public class ItemEditReqRepositoryImpl implements ItemEditReqRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public ItemEditReq findByIdWithItem(Long editReqId) {
        return jpaQueryFactory.selectFrom(itemEditReq)
                .leftJoin(itemEditReq.item, item).fetchJoin()
                .where(itemEditReq.id.eq(editReqId))
                .fetchOne();
    }
}
