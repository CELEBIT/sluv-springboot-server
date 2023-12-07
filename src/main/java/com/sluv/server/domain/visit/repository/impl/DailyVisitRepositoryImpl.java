package com.sluv.server.domain.visit.repository.impl;

import static com.sluv.server.domain.visit.entity.QDailyVisit.dailyVisit;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.visit.entity.DailyVisit;
import java.time.LocalDateTime;
import java.util.List;
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

    @Override
    public List<DailyVisit> getRecentDailyVisit(LocalDateTime now) {
        return jpaQueryFactory.selectFrom(dailyVisit)
                .where(dailyVisit.createdAt.dayOfMonth().eq(now.getDayOfMonth() - 1))
                .fetch();
    }

    @Override
    public void deleteTwoDaysAgo(LocalDateTime now) {
        jpaQueryFactory.delete(dailyVisit)
                .where(dailyVisit.createdAt.dayOfMonth().eq(now.getDayOfMonth() - 2))
                .execute();
    }
}
