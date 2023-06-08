package com.sluv.server.domain.closet.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.item.entity.Item;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sluv.server.domain.closet.entity.QCloset.closet;
import static com.sluv.server.domain.item.entity.QItemScrap.itemScrap;

@RequiredArgsConstructor
public class ClosetRepositoryImpl implements ClosetRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Closet> getRecentAddCloset(Item item) {
        // 상위 20개 추출
        return jpaQueryFactory.select(closet)
                .from(itemScrap)
                .leftJoin(itemScrap.closet, closet)
                .where(itemScrap.item.eq(item))
                .limit(20)
                .orderBy(itemScrap.createdAt.desc())
                .fetch();
    }
}
