package com.sluv.server.domain.item.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.item.entity.TempItem;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.sluv.server.domain.celeb.entity.QCeleb.celeb;
import static com.sluv.server.domain.item.entity.QTempItem.tempItem;

@RequiredArgsConstructor
public class TempItemRepositoryImpl implements TempItemRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<TempItem> getTempItemList(User user, Pageable pageable) {
        List<TempItem> result = jpaQueryFactory.selectFrom(tempItem)
                .where(tempItem.user.eq(user))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(tempItem.updatedAt.desc())
                .fetch();
        return new PageImpl<>(result);
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
