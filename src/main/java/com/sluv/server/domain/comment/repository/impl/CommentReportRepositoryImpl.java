package com.sluv.server.domain.comment.repository.impl;

import static com.sluv.server.domain.comment.entity.QCommentReport.commentReport;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentReportRepositoryImpl implements CommentReportRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void withdrawByUserId(Long userId) {
        jpaQueryFactory.update(commentReport)
                .set(commentReport.reporter.id, -1L)
                .where(commentReport.reporter.id.eq(userId))
                .execute();
    }
}
