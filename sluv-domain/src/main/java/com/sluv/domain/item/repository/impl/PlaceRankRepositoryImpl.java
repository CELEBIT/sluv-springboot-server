package com.sluv.domain.item.repository.impl;

import static com.sluv.domain.item.entity.QPlaceRank.placeRank;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.item.entity.PlaceRank;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlaceRankRepositoryImpl implements PlaceRankRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PlaceRank> getRecentPlaceTop20(Long userId) {

        return jpaQueryFactory.selectFrom(placeRank)
                .where(placeRank.user.id.eq(userId))
                .groupBy(placeRank.place)
                .limit(20)
                .orderBy(placeRank.createdAt.max().desc())
                .fetch();
    }
}
