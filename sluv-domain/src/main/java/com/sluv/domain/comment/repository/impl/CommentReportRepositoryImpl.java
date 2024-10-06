package com.sluv.domain.comment.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.comment.entity.CommentReport;
import com.sluv.domain.comment.entity.QCommentReport;
import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static com.sluv.domain.comment.entity.QComment.comment;
import static com.sluv.domain.comment.entity.QCommentReport.commentReport;

@RequiredArgsConstructor
public class CommentReportRepositoryImpl implements CommentReportRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QUser reporterUser = new QUser("reporterUser");
    private final QUser reportedUser = new QUser("reportedUser");

    @Override
    public Page<CommentReport> getAllCommentReport(Pageable pageable, ReportStatus reportStatus) {
        BooleanExpression predicate = commentReport.isNotNull();
        if (reportStatus != null) {
            predicate = predicate.and(commentReport.reportStatus.eq(reportStatus));
        }

//        List<CommentReportInfoDto> content = jpaQueryFactory
//                .select(Projections.constructor(CommentReportInfoDto.class,
//                        commentReport.reporter.id,
//                        commentReport.reporter.nickname,
//                        comment.user.id,
//                        comment.user.nickname,
//                        commentReport.id,
//                        commentReport.commentReportReason,
//                        commentReport.content,
//                        commentReport.reportStatus,
//                        commentReport.createdAt,
//                        commentReport.updatedAt))
        List<CommentReport> content = jpaQueryFactory.selectFrom(commentReport)
                .where(predicate)
                .orderBy(commentReport.createdAt.desc())
                .leftJoin(commentReport.comment, comment).fetchJoin()
                .leftJoin(commentReport.reporter, reporterUser).fetchJoin()
                .leftJoin(comment.user, reportedUser).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<CommentReport> countQuery = jpaQueryFactory.selectFrom(commentReport)
                .where(predicate);

        return PageableExecutionUtils.getPage(content, pageable,
                () -> countQuery.fetch().size());
    }

    @Override
    public Optional<CommentReport> getCommentReportDetail(Long commentReportId) {
//        CommentReportDetailDto detailDto = jpaQueryFactory
//                .select(Projections.constructor(CommentReportDetailDto.class,
//                        commentReport.reporter.id,
//                        commentReport.reporter.nickname,
//                        comment.user.id,
//                        comment.user.nickname,
//                        commentReport.id,
//                        commentReport.commentReportReason,
//                        commentReport.content,
//                        commentReport.reportStatus,
//                        comment.content,
//                        commentReport.createdAt,
//                        commentReport.updatedAt))
//                .from(commentReport)
//                .join(commentReport.comment, comment)
//                .join(commentReport.reporter, reporterUser)
//                .join(comment.user, reportedUser)
//                .where(commentReport.id.eq(commentReportId))
//                .fetchOne();
        CommentReport commentReport = jpaQueryFactory.selectFrom(QCommentReport.commentReport)
                .leftJoin(QCommentReport.commentReport.comment, comment).fetchJoin()
                .leftJoin(QCommentReport.commentReport.reporter, reporterUser).fetchJoin()
                .leftJoin(comment.user, reportedUser).fetchJoin()
                .where(QCommentReport.commentReport.id.eq(commentReportId))
                .fetchOne();

        return Optional.ofNullable(commentReport);
    }
}
