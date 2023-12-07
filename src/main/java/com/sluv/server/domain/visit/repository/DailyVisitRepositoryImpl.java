package com.sluv.server.domain.visit.repository;

import static com.sluv.server.domain.visit.entity.QDailyVisit.dailyVisit;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DailyVisitRepositoryImpl implements DailyVisitRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existUserToday(LocalDateTime now, User user) {
        int size = jpaQueryFactory.selectFrom(dailyVisit)
                .where(dailyVisit.user.eq(user).and(dailyVisit.createdAt.dayOfMonth().eq(now.getDayOfMonth())))
                .fetch().size();
        return size > 0;
    }
}
