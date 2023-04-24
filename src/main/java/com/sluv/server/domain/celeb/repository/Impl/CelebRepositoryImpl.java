package com.sluv.server.domain.celeb.repository.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static com.sluv.server.domain.brand.entity.QBrand.brand;
import static com.sluv.server.domain.brand.entity.QRecentBrand.recentBrand;
import static com.sluv.server.domain.celeb.entity.QRecentSearchCeleb.recentSearchCeleb;
import static com.sluv.server.domain.user.entity.QUser.user;
import static com.sluv.server.domain.celeb.entity.QCeleb.celeb;
import static com.sluv.server.domain.celeb.entity.QInterestedCeleb.interestedCeleb;

@RequiredArgsConstructor
public class CelebRepositoryImpl implements CelebRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Celeb> findInterestedCeleb(User _user) {

        return jpaQueryFactory.select(interestedCeleb.celeb)
                .from(interestedCeleb)
                .where(interestedCeleb.user.eq(_user))
                .orderBy(interestedCeleb.createdAt.desc())
                .fetch();
    }

    @Override
    public Page<Celeb> searchCeleb(String celebName, Pageable pageable) {
        List<Celeb> plusContent = new ArrayList<>();
        if(celebName.split(" ").length != 1){

            int lastSpace = celebName.lastIndexOf(" ");

            String teamName = celebName.substring(0, lastSpace);
            String memberName = celebName.substring(lastSpace+1);

            plusContent = jpaQueryFactory
                    .selectFrom(celeb)
                    .where(celeb.parent.celebNameKr.like(teamName+"%").and(celeb.celebNameKr.like(memberName+"%"))
                            .or(celeb.parent.celebNameEn.like(teamName+"%").and(celeb.celebNameEn.like(memberName+"%")))
                    ).offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }

        List<Celeb> content = jpaQueryFactory
                .selectFrom(celeb)
                .where(celeb.celebNameKr.like(celebName+"%")
                        .or(celeb.celebNameEn.like(celebName+"%"))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()-plusContent.size())
                .fetch();

        content.addAll(plusContent);


        return new PageImpl<>(content);
    }

    @Override
    public List<Celeb> findRecentCeleb(User _user) {
        return jpaQueryFactory.select(celeb)
                .from(recentSearchCeleb)
                .innerJoin(celeb).on(recentSearchCeleb.celeb.eq(celeb))
                .where(recentSearchCeleb.user.eq(_user))
                .groupBy(recentSearchCeleb.celeb)
                .orderBy(recentSearchCeleb.createdAt.max().desc())
                .limit(20)
                .fetch();

    }

    @Override
    public List<Celeb> findTop10Celeb() {

        return jpaQueryFactory.select(celeb)
                .from(recentSearchCeleb)
                .groupBy(recentSearchCeleb.celeb)
                .orderBy(recentSearchCeleb.celeb.count().desc())
                .limit(10)
                .fetch();
    }
}
