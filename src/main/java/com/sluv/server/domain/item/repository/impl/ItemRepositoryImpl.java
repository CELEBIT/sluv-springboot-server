package com.sluv.server.domain.item.repository.impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.item.entity.Item;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sluv.server.domain.item.entity.QItem.item;

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
}
