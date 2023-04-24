package com.sluv.server.domain.item.repository.hashtag.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.item.entity.hashtag.HashtagStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.sluv.server.domain.item.entity.hashtag.QItemHashtag.itemHashtag;

@RequiredArgsConstructor
public class HashtagRepositoryImpl implements HashtagRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Tuple> findAllByContent(String name, Pageable pageable) {
        List<Tuple> content = jpaQueryFactory.select(itemHashtag.hashtag, itemHashtag.hashtag.count())
                .from(itemHashtag)
                .where(itemHashtag.hashtag.content.like(name+"%")
                        .and(itemHashtag.hashtag.hashtagStatus.eq(HashtagStatus.ACTIVE))
                )
                .groupBy(itemHashtag.hashtag)
                .orderBy(itemHashtag.hashtag.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        return new PageImpl<>(content);
    }
}
