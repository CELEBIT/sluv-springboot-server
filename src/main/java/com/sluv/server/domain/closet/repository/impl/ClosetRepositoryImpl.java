package com.sluv.server.domain.closet.repository.impl;

import static com.sluv.server.domain.closet.entity.QCloset.closet;
import static com.sluv.server.domain.item.entity.QItemScrap.itemScrap;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.closet.dto.ClosetResDto;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.enums.ClosetStatus;
import com.sluv.server.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class ClosetRepositoryImpl implements ClosetRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Closet> getRecentAddCloset(Long itemId) {
        // 상위 20개 추출
        return jpaQueryFactory.select(closet)
                .from(itemScrap)
                .leftJoin(itemScrap.closet, closet)
                .where(itemScrap.item.id.eq(itemId))
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

    @Override
    public List<ClosetResDto> getUserClosetList(User user) {
        List<Tuple> content = jpaQueryFactory.select(closet, itemScrap.count())
                .from(closet)
                .leftJoin(itemScrap).on(itemScrap.closet.eq(closet))
                .where(closet.user.eq(user))
                .groupBy(closet)
                .fetch();

        return content.stream()
                .map(tuple -> ClosetResDto.of(tuple.get(closet), tuple.get(itemScrap.count())))
                .toList();
    }

    @Override
    public Boolean checkDuplicate(String name, Long closetId) {
        JPAQuery<Closet> query = jpaQueryFactory.selectFrom(closet)
                .where(closet.name.eq(name));

        if (closetId != null) {
            query.where(closet.id.ne(closetId));
        }

        return query.fetch().size() > 0;
    }

    @Override
    public void withdrawAllByUserId(Long userId) {
        jpaQueryFactory.update(closet)
                .set(closet.user.id, -1L)
                .where(closet.user.id.eq(userId))
                .execute();
    }
}
