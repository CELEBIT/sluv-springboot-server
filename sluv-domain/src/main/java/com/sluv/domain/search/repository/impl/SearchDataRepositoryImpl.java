package com.sluv.domain.search.repository.impl;

import static com.sluv.domain.search.entity.QSearchData.searchData;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.search.entity.SearchData;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class SearchDataRepositoryImpl implements SearchDataRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Tuple> getTopData() {
        LocalDateTime now = LocalDateTime.now();

        return jpaQueryFactory.select(searchData, searchData.count())
                .from(searchData)
                .where(searchData.createdAt.between(now.minusDays(31), now))
                .groupBy(searchData.searchWord)
                .orderBy(searchData.searchWord.count().desc())
                .limit(12)
                .fetch();
    }

    @Override
    public Page<SearchData> getSearchKeyword(String keyword, Pageable pageable) {
        JPAQuery<SearchData> query = jpaQueryFactory.selectFrom(searchData)
                .where(searchData.searchWord.like(keyword + "%"))
                .orderBy(searchData.searchWord.length().asc())
                .groupBy(searchData.searchWord);

        List<SearchData> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }
}
