package com.sluv.server.domain.item.repository.hashtag.impl;

import static com.sluv.server.domain.item.entity.hashtag.QItemHashtag.itemHashtag;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.item.entity.hashtag.HashtagStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class HashtagRepositoryImpl implements HashtagRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Tuple> findAllByContent(String name, Pageable pageable) {
        List<Tuple> content;

        // 미입력 시
        if (name == null) {
            content = jpaQueryFactory.select(itemHashtag.hashtag, itemHashtag.hashtag.count())
                    .from(itemHashtag)
                    .where(itemHashtag.hashtag.hashtagStatus.eq(HashtagStatus.ACTIVE))
                    .groupBy(itemHashtag.hashtag)
                    .orderBy(itemHashtag.hashtag.count().desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        } else { // name 입력 시
            content = jpaQueryFactory.select(itemHashtag.hashtag, itemHashtag.hashtag.count())
                    .from(itemHashtag)
                    .where(itemHashtag.hashtag.content.like(name + "%")
                            .and(itemHashtag.hashtag.hashtagStatus.eq(HashtagStatus.ACTIVE))
                    )
                    .groupBy(itemHashtag.hashtag)
                    .orderBy(itemHashtag.hashtag.count().desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }

        JPAQuery<Tuple> countContent = jpaQueryFactory.select(itemHashtag.hashtag, itemHashtag.hashtag.count())
                .from(itemHashtag)
                .where(itemHashtag.hashtag.content.like(name + "%")
                        .and(itemHashtag.hashtag.hashtagStatus.eq(HashtagStatus.ACTIVE))
                )
                .groupBy(itemHashtag.hashtag)
                .orderBy(itemHashtag.hashtag.count().desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> countContent.fetch().size());
    }
}
