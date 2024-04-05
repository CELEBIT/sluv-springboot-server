package com.sluv.server.domain.question.repository.impl;

import static com.sluv.server.domain.question.entity.QQuestionReport.questionReport;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuestionReportRepositoryImpl implements QuestionReportRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void withdrawByUserId(Long userId) {
        jpaQueryFactory.update(questionReport)
                .set(questionReport.reporter.id, -1L)
                .where(questionReport.reporter.id.eq(userId))
                .execute();
    }
}
