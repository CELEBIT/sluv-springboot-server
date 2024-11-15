package com.sluv.domain.celeb.repository.Impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.celeb.entity.*;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.sluv.domain.celeb.entity.QCeleb.celeb;
import static com.sluv.domain.celeb.entity.QCelebCategory.celebCategory;
import static com.sluv.domain.celeb.entity.QInterestedCeleb.interestedCeleb;
import static com.sluv.domain.celeb.entity.QRecentSelectCeleb.recentSelectCeleb;

@Slf4j
@RequiredArgsConstructor
public class CelebRepositoryImpl implements CelebRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Celeb> findInterestedCeleb(User _user) {
        log.info("유저 {}의 관심셀럽 조회", _user.getId());
        QCeleb child = new QCeleb("childCeleb");
        QCelebCategory parentCelebCategory = new QCelebCategory("parentCelebCategory");

        List<InterestedCeleb> fetch = jpaQueryFactory.selectFrom(interestedCeleb)
                .leftJoin(interestedCeleb.celeb, celeb).fetchJoin()
                .leftJoin(celeb.subCelebList, child).fetchJoin()
                .leftJoin(celeb.celebCategory, celebCategory).fetchJoin()
                .leftJoin(celebCategory.parent, parentCelebCategory).fetchJoin()
                .where(interestedCeleb.user.eq(_user))
                .orderBy(interestedCeleb.createdAt.desc())
                .fetch();

        return fetch.stream()
                .map(InterestedCeleb::getCeleb)
                .toList();
    }

    @Override
    public Page<Celeb> searchCeleb(String celebName, Pageable pageable) {
        /**
         * 1. [그룹명] [멤버명]
         * 2. [멤버명]
         * 3. [그룹명]
         */

        List<Celeb> plusContent = new ArrayList<>();

        // 1. [그룹명] [멤버명]
        if (celebName.split(" ").length != 1) {

            int lastSpace = celebName.lastIndexOf(" ");

            String teamName = celebName.substring(0, lastSpace);
            String memberName = celebName.substring(lastSpace + 1);

            plusContent = jpaQueryFactory
                    .selectFrom(celeb)
                    .where(celeb.parent.celebNameKr.like("%" + teamName + "%")
                            .and(celeb.celebNameKr.like("%" + memberName + "%"))
                            .or(celeb.parent.celebNameEn.like("%" + teamName + "%")
                                    .and(celeb.celebNameEn.like("%" + memberName + "%")))
                    ).offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }

        List<Celeb> content = jpaQueryFactory
                .selectFrom(celeb)
                .leftJoin(celeb.parent)
                .where(
                        // 2. [셀럽 이름과 일치]
                        celeb.celebNameKr.like("%" + celebName + "%")
                                .or(celeb.celebNameEn.like("%" + celebName + "%"))
                                // 3. [그룹 이름과 일치]
                                .or(celeb.parent.celebNameKr.like("%" + celebName + "%"))
                                .or(celeb.parent.celebNameEn.like("%" + celebName + "%"))
                                // 그룹이 검색되는 것을 방지.
                                .and(celeb.subCelebList.isEmpty())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() - plusContent.size())
                .orderBy(celeb.celebNameKr.asc())
                .fetch();

        content.addAll(plusContent);

        /**
         * count 쿼리
         */
        JPAQuery<Celeb> countCelebPlusJPAQuery;
        if (celebName.split(" ").length != 1) {

            int lastSpace = celebName.lastIndexOf(" ");

            String teamName = celebName.substring(0, lastSpace);
            String memberName = celebName.substring(lastSpace + 1);

            countCelebPlusJPAQuery = jpaQueryFactory
                    .selectFrom(celeb)
                    .where(celeb.parent.celebNameKr.like("%" + teamName + "%")
                            .and(celeb.celebNameKr.like("%" + memberName + "%"))
                            .or(celeb.parent.celebNameEn.like("%" + teamName + "%")
                                    .and(celeb.celebNameEn.like("%" + memberName + "%")))
                    );
        } else {
            countCelebPlusJPAQuery = null;
        }

        JPAQuery<Celeb> countCelebJPAQuery = jpaQueryFactory
                .selectFrom(celeb)
                .leftJoin(celeb.parent)
                .where(
                        // 2. [셀럽 이름과 일치]
                        celeb.celebNameKr.like("%" + celebName + "%")
                                .or(celeb.celebNameEn.like("%" + celebName + "%"))
                                // 3. [그룹 이름과 일치]
                                .or(celeb.parent.celebNameKr.like("%" + celebName + "%"))
                                .or(celeb.parent.celebNameEn.like("%" + celebName + "%"))
                                // 그룹이 검색되는 것을 방지.
                                .and(celeb.subCelebList.isEmpty())
                )
                .orderBy(celeb.celebNameKr.asc());

        return PageableExecutionUtils.getPage(
                content,
                pageable,
                () -> {
                    int size = countCelebJPAQuery.fetch().size();
                    int plusSize = countCelebPlusJPAQuery != null ? countCelebJPAQuery.fetch().size() : 0;
                    return size + plusSize;
                }
        );
    }

    @Override
    public List<Celeb> findRecentCeleb(User _user) {
        return jpaQueryFactory.select(celeb)
                .from(recentSelectCeleb)
                .innerJoin(celeb).on(recentSelectCeleb.celeb.eq(celeb))
                .where(recentSelectCeleb.user.eq(_user))
                .groupBy(recentSelectCeleb.celeb)
                .orderBy(recentSelectCeleb.createdAt.max().desc())
                .limit(20)
                .fetch();

    }

    @Override
    public List<Celeb> findTop10Celeb() {
        QCeleb parent = new QCeleb("parent");
        QCelebCategory parentCelebCategory = new QCelebCategory("parentCelebCategory");

        List<RecentSelectCeleb> fetch = jpaQueryFactory.selectFrom(recentSelectCeleb)
                .leftJoin(recentSelectCeleb.celeb, celeb).fetchJoin()
                .leftJoin(celeb.parent, parent).fetchJoin()
                .leftJoin(celeb.celebCategory, celebCategory).fetchJoin()
                .leftJoin(parent.celebCategory, parentCelebCategory).fetchJoin()
                .groupBy(recentSelectCeleb.celeb)
                .orderBy(recentSelectCeleb.celeb.count().desc())
                .limit(10)
                .fetch();
        return fetch.stream().map(RecentSelectCeleb::getCeleb).toList();
    }

    @Override
    public List<Celeb> getCelebByCategory(CelebCategory celebCategory) {

        return jpaQueryFactory.selectFrom(celeb)
                .where(celeb.celebCategory.eq(celebCategory)
                        .or(celeb.celebCategory.parent.eq(celebCategory)
                        ).and(celeb.parent.isNull())
                )
//                .limit(30)
                .orderBy(celeb.celebNameKr.asc())
                .fetch();

    }

    @Override
    public List<Celeb> searchInterestedCelebByParent(String celebName) {

        // celeb's parent와 이름이 일치
        return jpaQueryFactory
                .selectFrom(celeb)
                .where(celeb.parent.isNull()
                        .and(
                                celeb.celebNameKr.like("%" + celebName + "%")
                                        .or(celeb.celebNameEn.like("%" + celebName + "%"))
                        )
                )
                .fetch();
    }

    @Override
    public List<Celeb> searchInterestedCelebByChild(String celebName) {

        // celeb's child와 이름이 일치
        return jpaQueryFactory
                .select(celeb.parent)
                .from(celeb)
                .where(celeb.parent.isNotNull()
                        .and(
                                celeb.celebNameKr.like("%" + celebName + "%")
                                        .or(celeb.celebNameEn.like("%" + celebName + "%"))
                        )
                )
                .fetch();
    }

    @Override
    public Page<Celeb> getCelebByContainKeyword(String keyword, Pageable pageable) {
        QCeleb parent = new QCeleb("parent");
        List<Celeb> content = jpaQueryFactory.selectFrom(celeb)
                .leftJoin(celeb.parent, parent).fetchJoin()
                .where(celeb.celebNameKr.like("%" + keyword + "%")
                        .or(celeb.celebNameEn.like("%" + keyword + "%"))
                        .or(celeb.parent.celebNameKr.like("%" + keyword + "%"))
                        .or(celeb.parent.celebNameEn.like("%" + keyword + "%"))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())//5
                .fetch();

        //Count Query
        JPAQuery<Celeb> countQuery = jpaQueryFactory.selectFrom(celeb)
                .leftJoin(celeb.parent, parent).fetchJoin()
                .where(celeb.celebNameKr.like("%" + keyword + "%")
                        .or(celeb.celebNameEn.like("%" + keyword + "%"))
                        .or(celeb.parent.celebNameKr.like("%" + keyword + "%"))
                        .or(celeb.parent.celebNameEn.like("%" + keyword + "%"))
                );

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    @Override
    public Page<Celeb> findAllWithPageable(Pageable pageable) {
        List<Celeb> content = jpaQueryFactory.selectFrom(celeb)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //Count Query
        JPAQuery<Celeb> countQuery = jpaQueryFactory.selectFrom(celeb);

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }
}
