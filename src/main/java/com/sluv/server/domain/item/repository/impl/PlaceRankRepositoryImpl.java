package com.sluv.server.domain.item.repository.impl;

import static com.sluv.server.domain.item.entity.QPlaceRank.placeRank;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.item.entity.PlaceRank;
import com.sluv.server.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlaceRankRepositoryImpl implements PlaceRankRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PlaceRank> getRecentPlaceTop20(User user) {

        return jpaQueryFactory.selectFrom(placeRank)
                .where(placeRank.user.eq(user))
                .groupBy(placeRank.place)
                .limit(20)
                .orderBy(placeRank.createdAt.max().desc())
                .fetch();
    }
}
