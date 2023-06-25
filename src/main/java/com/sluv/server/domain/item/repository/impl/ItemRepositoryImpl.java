package com.sluv.server.domain.item.repository.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.search.dto.SearchFilterReqDto;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.sluv.server.domain.closet.entity.QCloset.closet;
import static com.sluv.server.domain.item.entity.QItem.item;
import static com.sluv.server.domain.item.entity.QItemLike.itemLike;
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
    public Page<Item> getSearchItem(List<Long> itemIdList, SearchFilterReqDto dto, Pageable pageable) {

        JPAQuery<Item> query = jpaQueryFactory.select(item)
                .from(item)
                .leftJoin(itemLike).on(itemLike.item.eq(item))
                .leftJoin(itemScrap).on(itemScrap.item.eq(item))
                .groupBy(item)
                .where(item.id.in(itemIdList).and(item.itemStatus.eq(ACTIVE)))
                .orderBy(getSearchItemOrder(pageable.getSort()));
        // Filter 조건 추가
        addFilterWhere(query, dto);
        // Pagination 추가
        List<Item> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<Item> countQuery = jpaQueryFactory.select(item)
                .from(item)
                .leftJoin(itemLike).on(itemLike.item.eq(item))
                .leftJoin(itemScrap).on(itemScrap.item.eq(item))
                .groupBy(item)
                .where(item.id.in(itemIdList).and(item.itemStatus.eq(ACTIVE)))
                .orderBy(getSearchItemOrder(pageable.getSort()));

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    @Override
    public Page<Item> getRecommendItemPage(Pageable pageable) {
        List<Item> content = jpaQueryFactory.select(item)
                .from(item)
                .leftJoin(itemLike).on(itemLike.item.eq(item))
                .leftJoin(itemScrap).on(itemScrap.item.eq(item))
                .groupBy(item)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(itemLike.count().add(itemScrap.count()).add(item.viewNum).desc())
                .orderBy(item.whenDiscovery.desc())
                .fetch();


        // Count Query
        JPAQuery<Item> countJPAQuery = jpaQueryFactory.select(item)
                .from(item)
                .leftJoin(itemLike).on(itemLike.item.eq(item))
                .leftJoin(itemScrap).on(itemScrap.item.eq(item))
                .groupBy(item)
                .orderBy(itemLike.count().add(itemScrap.count()).add(item.viewNum).desc())
                .orderBy(item.whenDiscovery.desc());


        return PageableExecutionUtils.getPage(content, pageable, () -> countJPAQuery.fetch().size());
    }

    @Override
    public Long getSearchItemCount(List<Long> itemIdList, SearchFilterReqDto dto) {

        JPAQuery<Item> query = jpaQueryFactory.selectFrom(item)
                .where(item.id.in(itemIdList));

        addFilterWhere(query, dto);

        return (long) query.fetch().size();
    }

    /**
     * 검색 필터링 쿼리 추가
     */
    private JPAQuery<Item> addFilterWhere(JPAQuery<Item> query, SearchFilterReqDto dto){
        // Category Filtering
        if (dto.getCategoryId() != null){
            query.where(item.category.id.eq(dto.getCategoryId()));
        }
        // Price Filtering
        if(dto.getMinPrice() != null && dto.getMaxPrice() != null){
            query.where(item.price.between(dto.getMinPrice(), dto.getMaxPrice()));
        }else if(dto.getMinPrice() != null){
            query.where(item.price.goe(dto.getMinPrice()));
        }
        // Color Filtering
        if(dto.getColor() != null){
            query.where(item.color.eq(dto.getColor()));
        }
        return query;
    }

    /**
     * 정렬 조건 추가
     */
    private OrderSpecifier<?> getSearchItemOrder(Sort sort) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        if (sort.isSorted()) {
            for (Sort.Order order : sort) {

                // 기본이 최신순
                OrderSpecifier<?> orderSpecifier = item.whenDiscovery.desc();
                String property = order.getProperty();
                switch (property) {
                    case "최신순" -> orderSpecifier = item.whenDiscovery.desc();
                    case "인기순" -> orderSpecifier = itemLike.count().add(itemScrap.count()).add(item.viewNum).desc();
                    case "저가순" -> orderSpecifier = item.price.asc();
                    case "고가순" -> orderSpecifier = item.price.desc();
                }

                orderSpecifiers.add(orderSpecifier);
            }
        }else{
            orderSpecifiers.add(item.whenDiscovery.desc());
        }

        return orderSpecifiers.get(0);


    }

    @Override
    public List<Item> getRecentTop2Item(User targetUser) {

        return jpaQueryFactory.selectFrom(item)
                .where(item.user.eq(targetUser))
                .limit(2)
                .orderBy(item.id.desc())
                .fetch();
    }

    /**
     * MyPage 유저가 작성한 모든 아이템 검색
     */
    @Override
    public Page<Item> getUserAllItem(Long userId, Pageable pageable) {
        List<Item> content = jpaQueryFactory.selectFrom(item)
                .where(item.user.id.eq(userId).and(item.itemStatus.eq(ACTIVE)))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<Item> query = jpaQueryFactory.selectFrom(item)
                .where(item.user.id.eq(userId).and(item.itemStatus.eq(ACTIVE)))
                .orderBy(item.id.desc());


        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }

    /**
     * 유저가 좋아요 누른 Item 조회
     */
    @Override
    public Page<Item> getAllByUserLikeItem(User user, Pageable pageable) {
        List<Item> content = jpaQueryFactory.select(item)
                .from(itemLike)
                .leftJoin(itemLike.item, item)
                .orderBy(itemLike.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<Item> query = jpaQueryFactory.select(item)
                .from(itemLike)
                .leftJoin(itemLike.item, item)
                .orderBy(itemLike.id.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }

    /**
     * 핫한 셀럽들이 선택한 여름나기 아이템
     */
    @Override
    public Page<Item> getCelebSummerItem(Pageable pageable, SearchFilterReqDto dto) {
        List<Long> categortIdList = new ArrayList<>();

        categortIdList.add(10L); // 반소매
        categortIdList.add(17L); // 민소매
        categortIdList.add(27L); // 반바지
        categortIdList.add(33L); // 미니스커트
        categortIdList.add(37L); // 미니원피스
        categortIdList.add(42L); // 스킨케어
        categortIdList.add(44L); // 헤어&바디


        JPAQuery<Item> query = jpaQueryFactory.selectFrom(item)
                .where(item.category.id.in(categortIdList)
                        .and(item.itemStatus.eq(ACTIVE))
                )
                .orderBy(getSearchItemOrder(pageable.getSort()));
            // Filter 추가
        addFilterWhere(query, dto);
            // Pagination 추가
        List<Item> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<Item> countQuery = jpaQueryFactory.selectFrom(item)
                .where(item.category.id.in(categortIdList)
                        .and(item.itemStatus.eq(ACTIVE))
                )
                .orderBy(getSearchItemOrder(pageable.getSort()));
            // Filter 추가
        addFilterWhere(countQuery, dto);


        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }
}
