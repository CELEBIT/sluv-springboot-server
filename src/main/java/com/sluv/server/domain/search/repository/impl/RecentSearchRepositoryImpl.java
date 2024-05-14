package com.sluv.server.domain.search.repository.impl;

import static com.sluv.server.domain.search.entity.QRecentSearch.recentSearch;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.search.entity.RecentSearch;
import com.sluv.server.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecentSearchRepositoryImpl implements RecentSearchRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RecentSearch> getRecentSearch(User user) {
        List<RecentSearch> content = jpaQueryFactory.selectFrom(recentSearch)
                .where(recentSearch.user.eq(user))
                .groupBy(recentSearch.searchWord)
                .orderBy(recentSearch.createdAt.max().desc())
                .limit(10)
                .fetch();

        return content;
    }
}
