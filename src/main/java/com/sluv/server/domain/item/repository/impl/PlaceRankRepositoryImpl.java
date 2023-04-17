package com.sluv.server.domain.item.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sluv.server.domain.item.entity.QPlaceRank.placeRank;

@RequiredArgsConstructor
public class PlaceRankRepositoryImpl implements PlaceRankRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<String> findTopPlace() {

        return jpaQueryFactory.select(placeRank.place)
                .from(placeRank)
                .groupBy(placeRank.place)
                .orderBy(placeRank.place.count().desc())
                .limit(10)
                .fetch();
    }
}
