package com.sluv.domain.notice.repository.impl;

import static com.sluv.domain.notice.entity.QNotice.notice;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.notice.entity.Notice;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Notice> getAllNotice(Pageable pageable) {
        List<Notice> content = jpaQueryFactory.selectFrom(notice)
                .orderBy(notice.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<Notice> count = jpaQueryFactory.selectFrom(notice)
                .orderBy(notice.createdAt.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> count.fetch().size());
    }
}
