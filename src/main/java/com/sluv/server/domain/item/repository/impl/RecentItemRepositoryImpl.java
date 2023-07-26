package com.sluv.server.domain.item.repository.impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.item.entity.RecentItem;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sluv.server.domain.item.entity.QRecentItem.recentItem;

@RequiredArgsConstructor
public class RecentItemRepositoryImpl implements RecentItemRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<RecentItem> getUserAllRecentItem(User user, Pageable pageable) {
        List<RecentItem> content = jpaQueryFactory.selectFrom(recentItem)
                .where(recentItem.user.eq(user))
                .orderBy(recentItem.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<RecentItem> query = jpaQueryFactory.selectFrom(recentItem)
                .where(recentItem.user.eq(user))
                .orderBy(recentItem.id.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }
}
