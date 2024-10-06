package com.sluv.domain.visit.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.visit.entity.VisitHistory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.sluv.domain.visit.entity.QVisitHistory.visitHistory;


@RequiredArgsConstructor
public class VisitHistoryRepositoryImpl implements VisitHistoryRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<VisitHistory> getVisitHistoryFor10Days() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime toDay = LocalDateTime.of(
                now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        return jpaQueryFactory.selectFrom(visitHistory)
                .where(visitHistory.date.between(toDay.minusDays(10), toDay.minusDays(1)))
                .orderBy(visitHistory.date.asc())
                .fetch();
    }

}
