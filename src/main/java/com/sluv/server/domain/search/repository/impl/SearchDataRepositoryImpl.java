package com.sluv.server.domain.search.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sluv.server.domain.search.entity.QSearchData.searchData;

@RequiredArgsConstructor
public class SearchDataRepositoryImpl implements SearchDataRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Tuple> getTopData() {

        return jpaQueryFactory.select(searchData, searchData.count())
                .from(searchData)
                .groupBy(searchData.searchWord)
                .orderBy(searchData.searchWord.count().desc())
                .limit(9)
                .fetch();
    }
}
