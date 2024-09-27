package com.sluv.domain.item.repository.impl;

import static com.sluv.domain.brand.entity.QBrand.brand;
import static com.sluv.domain.brand.entity.QNewBrand.newBrand;
import static com.sluv.domain.celeb.entity.QCeleb.celeb;
import static com.sluv.domain.celeb.entity.QCelebCategory.celebCategory;
import static com.sluv.domain.celeb.entity.QNewCeleb.newCeleb;
import static com.sluv.domain.item.entity.QItemCategory.itemCategory;
import static com.sluv.domain.item.entity.QTempItem.tempItem;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.celeb.entity.QCeleb;
import com.sluv.domain.celeb.entity.QCelebCategory;
import com.sluv.domain.item.entity.QItemCategory;
import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class TempItemRepositoryImpl implements TempItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<TempItem> getTempItemList(User user, Pageable pageable) {
        QCeleb parent = new QCeleb("parent");
        QCelebCategory parentCelebCategory = new QCelebCategory("parentCelebCategory");
        QItemCategory parentItemCategory = new QItemCategory("parentItemCategory");

        List<TempItem> content = jpaQueryFactory.selectFrom(tempItem).leftJoin(tempItem.celeb, celeb).fetchJoin()
                .leftJoin(celeb.parent, parent).fetchJoin().leftJoin(celeb.celebCategory, celebCategory).fetchJoin()
                .leftJoin(celebCategory.parent, parentCelebCategory).fetchJoin()
                .leftJoin(tempItem.category, itemCategory).fetchJoin().leftJoin(itemCategory.parent, parentItemCategory)
                .fetchJoin().leftJoin(tempItem.brand, brand).fetchJoin().leftJoin(tempItem.newCeleb, newCeleb)
                .fetchJoin().leftJoin(tempItem.newBrand, newBrand).fetchJoin().where(tempItem.user.eq(user))
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).orderBy(tempItem.updatedAt.desc()).fetch();

        JPAQuery<TempItem> countContent = jpaQueryFactory.selectFrom(tempItem).where(tempItem.user.eq(user));

        return PageableExecutionUtils.getPage(content, pageable, () -> countContent.fetch().size());
    }

    @Override
    public List<TempItem> findAllExceptLast(Long userId) {

        return jpaQueryFactory.selectFrom(tempItem)
                .where(tempItem.user.id.eq(userId))
                .orderBy(tempItem.updatedAt.desc())
                .offset(1).fetch();
    }
}

