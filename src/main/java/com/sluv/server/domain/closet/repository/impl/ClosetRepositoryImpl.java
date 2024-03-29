package com.sluv.server.domain.closet.repository.impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.enums.ClosetStatus;
import com.sluv.server.domain.item.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sluv.server.domain.closet.entity.QCloset.closet;
import static com.sluv.server.domain.item.entity.QItemScrap.itemScrap;

@RequiredArgsConstructor
public class ClosetRepositoryImpl implements ClosetRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Closet> getRecentAddCloset(Item item) {
        // 상위 20개 추출
        return jpaQueryFactory.select(closet)
                .from(itemScrap)
                .leftJoin(itemScrap.closet, closet)
                .where(itemScrap.item.eq(item))
                .limit(20)
                .orderBy(itemScrap.createdAt.desc())
                .fetch();
    }

    /**
     * MyPage 유저 일치 시 모든 Closet 조회
     */
    @Override
    public Page<Closet> getUserAllCloset(Long userId, Pageable pageable) {
        List<Closet> content = jpaQueryFactory.selectFrom(closet)
                .where(closet.user.id.eq(userId))
                .orderBy(closet.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query

        JPAQuery<Closet> query = jpaQueryFactory.selectFrom(closet)
                .where(closet.user.id.eq(userId))
                .orderBy(closet.id.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }

    /**
     * MyPage 유저 불일치 시 Public Closet 조회
     */
    @Override
    public Page<Closet> getUserAllPublicCloset(Long userId, Pageable pageable) {
        List<Closet> content = jpaQueryFactory.selectFrom(closet)
                .where(closet.user.id.eq(userId).and(closet.closetStatus.eq(ClosetStatus.PUBLIC)))
                .orderBy(closet.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query

        JPAQuery<Closet> query = jpaQueryFactory.selectFrom(closet)
                .where(closet.user.id.eq(userId).and(closet.closetStatus.eq(ClosetStatus.PUBLIC)))
                .orderBy(closet.id.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }
}
