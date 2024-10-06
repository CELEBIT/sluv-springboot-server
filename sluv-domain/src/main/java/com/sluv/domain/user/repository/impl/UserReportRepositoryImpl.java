package com.sluv.domain.user.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.user.entity.QUser;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.entity.UserReport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sluv.domain.user.entity.QUserReport.userReport;

@RequiredArgsConstructor
public class UserReportRepositoryImpl implements UserReportRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Boolean findExistence(User user, User target) {

        return jpaQueryFactory.selectFrom(userReport)
                .where(userReport.reporter.eq(user)
                        .and(userReport.reported.eq(target))
                )
                .fetchFirst() != null;
    }

    @Override
    public void withdrawByUserId(Long userId) {
        jpaQueryFactory.update(userReport)
//                .set(userReport.reporter.id, -1L)
                .set(userReport.reporter.id, 2L)
                .where(userReport.reporter.id.eq(userId))
                .execute();

//        jpaQueryFactory.update(userReport)
//                .set(userReport.reported.id, -1L)
//                .where(userReport.reported.id.eq(userId))
//                .execute();
    }

    @Override
    public Page<UserReport> getAllUserReport(Pageable pageable, ReportStatus reportStatus) {
        QUser reporterUser = new QUser("reporterUser");
        QUser reportedUser = new QUser("reportedUser");

        BooleanExpression predicate = userReport.isNotNull();
        if (reportStatus != null) {
            predicate = predicate.and(userReport.reportStatus.eq(reportStatus));
        }

//        List<UserReportInfoDto> content = jpaQueryFactory
//                .select(Projections.constructor(UserReportInfoDto.class,
//                        userReport.reporter.id,
//                        userReport.reporter.nickname,
//                        userReport.reported.id,
//                        userReport.reported.nickname,
//                        userReport.id,
//                        userReport.userReportReason,
//                        userReport.content,
//                        userReport.reportStatus,
//                        userReport.createdAt,
//                        userReport.updatedAt))
        List<UserReport> content = jpaQueryFactory.selectFrom(userReport)
                .where(predicate)
                .orderBy(userReport.createdAt.desc())
                .leftJoin(userReport.reporter, reporterUser).fetchJoin()
                .leftJoin(userReport.reported, reportedUser).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<UserReport> countQuery = jpaQueryFactory.selectFrom(userReport)
                .where(predicate);

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }
}
