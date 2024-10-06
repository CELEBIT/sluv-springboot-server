package com.sluv.domain.question.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.question.entity.QuestionReport;
import com.sluv.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static com.sluv.domain.question.entity.QQuestion.question;
import static com.sluv.domain.question.entity.QQuestionReport.questionReport;


@RequiredArgsConstructor
public class QuestionReportRepositoryImpl implements QuestionReportRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QUser reporterUser = new QUser("reporterUser");
    private final QUser reportedUser = new QUser("reportedUser");

    @Override
    public Page<QuestionReport> getAllQuestionReport(Pageable pageable, ReportStatus reportStatus) {
        BooleanExpression predicate = questionReport.isNotNull();
        if (reportStatus != null) {
            predicate = predicate.and(questionReport.reportStatus.eq(reportStatus));
        }

//        List<QuestionReportInfoDto> content = jpaQueryFactory
//                .select(Projections.constructor(QuestionReportInfoDto.class,
//                        questionReport.reporter.id,
//                        questionReport.reporter.nickname,
//                        question.user.id,
//                        question.user.nickname,
//                        questionReport.id,
//                        questionReport.questionReportReason,
//                        questionReport.content,
//                        questionReport.reportStatus,
//                        questionReport.createdAt,
//                        questionReport.updatedAt))
        List<QuestionReport> content = jpaQueryFactory.selectFrom(questionReport)
                .where(predicate)
                .orderBy(questionReport.createdAt.desc())
                .leftJoin(questionReport.question, question).fetchJoin()
                .leftJoin(questionReport.reporter, reporterUser).fetchJoin()
                .leftJoin(question.user, reportedUser).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<QuestionReport> countQuery = jpaQueryFactory.selectFrom(questionReport)
                .where(predicate);

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    @Override
    public Optional<QuestionReport> getQuestionReportDetail(Long questionReportId) {
//        QuestionReportDetailDto detailDto = jpaQueryFactory
//                .select(Projections.constructor(QuestionReportDetailDto.class,
//                        questionReport.reporter.id,
//                        questionReport.reporter.nickname,
//                        question.user.id,
//                        question.user.nickname,
//                        questionReport.id,
//                        questionReport.questionReportReason,
//                        questionReport.content,
//                        questionReport.reportStatus,
//                        question.title,
//                        question.content,
//                        questionReport.createdAt,
//                        questionReport.updatedAt))
        QuestionReport content = jpaQueryFactory.selectFrom(questionReport)
                .where(questionReport.id.eq(questionReportId))
                .leftJoin(questionReport.question, question).fetchJoin()
                .leftJoin(questionReport.reporter, reporterUser).fetchJoin()
                .leftJoin(question.user, reportedUser).fetchJoin()
                .fetchOne();

        return Optional.ofNullable(content);
    }
}
