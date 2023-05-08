package com.sluv.server.domain.user.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import static com.sluv.server.domain.user.entity.QUserReport.userReport;

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
}
