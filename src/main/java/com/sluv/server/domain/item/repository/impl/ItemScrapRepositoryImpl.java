package com.sluv.server.domain.item.repository.impl;

import static com.sluv.server.domain.item.entity.QItemScrap.itemScrap;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.item.entity.Item;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ItemScrapRepositoryImpl implements ItemScrapRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Boolean getItemScrapStatus(Item item, List<Closet> closetList) {
        return jpaQueryFactory.select(itemScrap)
                .from(itemScrap)
                .where(itemScrap.closet.in(closetList).and(itemScrap.item.eq(item)))
                .fetch().size() > 0;
    }
}
