package com.sluv.server.domain.item.repository.impl;

import static com.sluv.server.domain.item.entity.QItemLike.itemLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ItemLikeRepositoryImpl implements ItemLikeRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void withdrawByUserId(Long userId) {
        jpaQueryFactory.update(itemLike)
                .set(itemLike.user.id, -1L)
                .where(itemLike.user.id.eq(userId))
                .execute();
    }
}
