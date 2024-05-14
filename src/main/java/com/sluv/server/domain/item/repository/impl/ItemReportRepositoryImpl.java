package com.sluv.server.domain.item.repository.impl;

import static com.sluv.server.domain.item.entity.QItemReport.itemReport;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ItemReportRepositoryImpl implements ItemReportRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Boolean findExistence(User user, Item target) {

        return jpaQueryFactory.selectFrom(itemReport)
                .where(itemReport.reporter.eq(user)
                        .and(itemReport.item.eq(target))
                )
                .fetchFirst() != null;
    }

}
