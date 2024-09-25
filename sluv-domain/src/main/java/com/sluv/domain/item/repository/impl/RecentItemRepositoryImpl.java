package com.sluv.domain.item.repository.impl;

import static com.sluv.domain.item.entity.QRecentItem.recentItem;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.item.entity.RecentItem;
import com.sluv.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class RecentItemRepositoryImpl implements RecentItemRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<RecentItem> getUserAllRecentItem(User user, Pageable pageable) {
        List<RecentItem> content = jpaQueryFactory.selectFrom(recentItem)
                .where(recentItem.user.eq(user))
                .orderBy(recentItem.createdAt.max().desc())
                .groupBy(recentItem.item)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<RecentItem> query = jpaQueryFactory.selectFrom(recentItem)
                .where(recentItem.user.eq(user))
                .groupBy(recentItem.item)
                .orderBy(recentItem.createdAt.max().desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }
}
