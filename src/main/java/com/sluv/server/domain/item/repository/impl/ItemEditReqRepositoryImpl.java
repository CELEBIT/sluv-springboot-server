package com.sluv.server.domain.item.repository.impl;

import static com.sluv.server.domain.item.entity.QItemEditReq.itemEditReq;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ItemEditReqRepositoryImpl implements ItemEditReqRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void withdrawByUserId(Long userId) {
        jpaQueryFactory.update(itemEditReq)
                .set(itemEditReq.requester.id, -1L)
                .where(itemEditReq.requester.id.eq(userId))
                .execute();
    }
}
