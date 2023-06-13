package com.sluv.server.domain.item.repository.impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sluv.server.domain.closet.entity.QCloset.closet;
import static com.sluv.server.domain.item.entity.QItem.item;
import static com.sluv.server.domain.item.entity.QItemScrap.itemScrap;
import static com.sluv.server.domain.item.entity.QRecentItem.recentItem;
import static com.sluv.server.domain.item.enums.ItemStatus.ACTIVE;
import static com.sluv.server.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<String> findTopPlace() {
        return jpaQueryFactory.select(item.whereDiscovery)
                .from(item)
                .groupBy(item.whereDiscovery)
                .orderBy(item.whereDiscovery.count().desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<Item> findSameCelebItem(Item _item, boolean celebJudge) {
        JPAQuery<Item> query = jpaQueryFactory.selectFrom(item);
        if(celebJudge){
            query = query
                .where(item.celeb.eq(_item.getCeleb())
                        .and(item.ne(_item))
                );
        }else{
            query = query
                    .where(item.newCeleb.eq(_item.getNewCeleb())
                            .and(item.ne(_item))
                    );
        }

        return query
                .limit(10)
                .orderBy(item.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Item> findSameBrandItem(Item _item, boolean brandJudge) {
        JPAQuery<Item> query = jpaQueryFactory.selectFrom(item);

        if(brandJudge){
            query = query
                    .where(item.brand.eq(_item.getBrand())
                            .and(item.ne(_item))
                    );
        }else{
            query = query
                    .where(item.newBrand.eq(_item.getNewBrand())
                            .and(item.ne(_item))
                    );
        }

        return query
                .limit(10)
                .orderBy(item.createdAt.desc())
                .fetch();
    }

    @Override
    public Page<Item> getRecentItem(User user, Pageable pageable) {

        // content Query
        List<Item> content = jpaQueryFactory.select(item)
                .from(recentItem)
                .leftJoin(recentItem.item, item)
                .where(recentItem.user.eq(user))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(item)
                .orderBy(recentItem.createdAt.max().desc())
                .fetch();

        // count Query
        JPAQuery<Item> countJPAQuery = jpaQueryFactory.select(item)
                .from(recentItem)
                .leftJoin(recentItem.item, item)
                .where(recentItem.user.eq(user))
                .groupBy(item)
                .orderBy(recentItem.createdAt.max().desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> countJPAQuery.fetch().size());

    }

    @Override
    public Page<Item> getAllScrapItem(User _user, Pageable pageable) {

        List<Item> content = jpaQueryFactory.select(item)
                .from(itemScrap)
                .leftJoin(itemScrap.closet, closet)
                .leftJoin(closet.user, user)
                .where(closet.user.eq(_user))
                .orderBy(itemScrap.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<Item> countJPAQuery = jpaQueryFactory.select(item)
                .from(itemScrap)
                .leftJoin(itemScrap.closet, closet)
                .leftJoin(closet.user, user)
                .where(closet.user.eq(_user))
                .orderBy(itemScrap.createdAt.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> countJPAQuery.fetch().size());
    }

    @Override
    public Page<Item> getClosetItems(Closet _closet, Pageable pageable) {
        List<Item> content = jpaQueryFactory.select(item)
                .from(itemScrap)
                .leftJoin(itemScrap.item, item)
                .where(itemScrap.closet.eq(_closet))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(itemScrap.createdAt.desc())
                .fetch();

        // Count Query
        JPAQuery<Item> countJPAQuery = jpaQueryFactory.select(item)
                .from(itemScrap)
                .leftJoin(itemScrap.item, item)
                .where(itemScrap.closet.eq(_closet))
                .orderBy(itemScrap.createdAt.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> countJPAQuery.fetch().size());
    }

    @Override
    public List<Item> getSameClosetItems(Item _item, List<Closet> closetList) {
        return jpaQueryFactory.select(item)
                .from(itemScrap)
                .leftJoin(itemScrap.item, item)
                .where(itemScrap.closet.in(closetList).and(item.ne(_item)))
                .groupBy(item)
                .orderBy(itemScrap.createdAt.max().desc())
                .limit(10)
                .fetch();
    }

    /**
     * Search 시 Item 검색
     */
    @Override
    public Page<Item> getSearchItem(List<Long> itemIdList, Pageable pageable) {
        // TODO 1. Ordering, Filtering
        List<Item> content = jpaQueryFactory.select(item)
                .from(item)
                .where(item.id.in(itemIdList).and(item.itemStatus.eq(ACTIVE)))
                .orderBy(item.createdAt.desc()) // 추가 예정
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<Item> countJPAQuery = jpaQueryFactory.select(item)
                .from(item)
                .where(item.id.in(itemIdList))
                .orderBy(item.whenDiscovery.desc());// 추가 예정


        return PageableExecutionUtils.getPage(content, pageable, () -> countJPAQuery.fetch().size());
    }
}
