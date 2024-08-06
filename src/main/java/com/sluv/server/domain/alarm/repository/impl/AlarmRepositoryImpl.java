package com.sluv.server.domain.alarm.repository.impl;

import static com.sluv.server.domain.alarm.entity.QAlarm.alarm;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.alarm.entity.Alarm;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class AlarmRepositoryImpl implements AlarmRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Alarm> findAllByUserId(Long userId, Pageable pageable) {
        List<Alarm> fetch = jpaQueryFactory.selectFrom(alarm)
                .where(alarm.user.id.eq(userId))
                .orderBy(alarm.createdAt.desc())
                .fetch();

        // Count Query
        JPAQuery<Alarm> countQuery = jpaQueryFactory.selectFrom(alarm)
                .where(alarm.user.id.eq(userId))
                .orderBy(alarm.createdAt.desc());

        return PageableExecutionUtils.getPage(fetch, pageable, () -> countQuery.stream().count());
    }
}
