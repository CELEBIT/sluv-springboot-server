package com.sluv.server.domain.user.repository.impl;

import static com.sluv.server.domain.user.entity.QUserReport.userReport;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

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
}
