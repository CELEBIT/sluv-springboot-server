package com.sluv.server.domain.item.repository.impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.item.entity.TempItem;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sluv.server.domain.celeb.entity.QCeleb.celeb;
import static com.sluv.server.domain.item.entity.QTempItem.tempItem;

@RequiredArgsConstructor
public class TempItemRepositoryImpl implements TempItemRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<TempItem> getTempItemList(User user, Pageable pageable) {
        List<TempItem> content = jpaQueryFactory.selectFrom(tempItem)
                .where(tempItem.user.eq(user))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(tempItem.updatedAt.desc())
                .fetch();

        JPAQuery<TempItem> countContent = jpaQueryFactory.selectFrom(tempItem)
                .where(tempItem.user.eq(user));

        return PageableExecutionUtils.getPage(content, pageable, () -> countContent.fetch().size());
    }

    @Override
    public List<TempItem> findAllExceptLast(User user) {

        return jpaQueryFactory.selectFrom(tempItem)
                .where(tempItem.user.eq(user))
                .orderBy(tempItem.updatedAt.desc())
                .offset(1)
                .fetch();
    }

}
