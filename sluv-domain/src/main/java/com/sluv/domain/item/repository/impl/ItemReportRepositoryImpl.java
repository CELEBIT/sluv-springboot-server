package com.sluv.domain.item.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemReport;
import com.sluv.domain.user.entity.QUser;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sluv.domain.brand.entity.QBrand.brand;
import static com.sluv.domain.celeb.entity.QCeleb.celeb;
import static com.sluv.domain.item.entity.QItem.item;
import static com.sluv.domain.item.entity.QItemCategory.itemCategory;
import static com.sluv.domain.item.entity.QItemImg.itemImg;
import static com.sluv.domain.item.entity.QItemLink.itemLink;
import static com.sluv.domain.item.entity.QItemReport.itemReport;

@RequiredArgsConstructor
public class ItemReportRepositoryImpl implements ItemReportRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QUser reporterUser = new QUser("reporterUser");
    private final QUser reportedUser = new QUser("reportedUser");

    @Override
    public Boolean findExistence(User user, Item target) {

        return jpaQueryFactory.selectFrom(itemReport)
                .where(itemReport.reporter.eq(user)
                        .and(itemReport.item.eq(target))
                )
                .fetchFirst() != null;
    }

    @Override
    public Page<ItemReport> getAllItemReport(Pageable pageable, ReportStatus reportStatus) {
        BooleanExpression predicate = itemReport.isNotNull();
        if (reportStatus != null) {
            predicate = predicate.and(itemReport.reportStatus.eq(reportStatus));
        }

//        List<ItemReportInfoDto> content = jpaQueryFactory
//                .select(Projections.constructor(ItemReportInfoDto.class,
//                        itemReport.reporter.id,
//                        itemReport.reporter.nickname,
//                        item.user.id,
//                        item.user.nickname,
//                        itemReport.id,
//                        itemReport.itemReportReason,
//                        itemReport.content,
//                        itemReport.reportStatus,
//                        itemReport.createdAt,
//                        itemReport.updatedAt))
        List<ItemReport> content = jpaQueryFactory.selectFrom(itemReport)
                .where(predicate)
                .orderBy(itemReport.createdAt.desc())
                .leftJoin(itemReport.item, item).fetchJoin()
                .leftJoin(itemReport.reporter, reporterUser).fetchJoin()
                .leftJoin(item.user, reportedUser).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<ItemReport> countQuery = jpaQueryFactory.selectFrom(itemReport)
                .where(predicate);

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    @Override
    public ItemReport getItemReportDetail(Long itemReportId) {

//        Optional<Item> optionalItem = Optional.ofNullable(
//                jpaQueryFactory
//                        .select(item)
//                        .from(itemReport)
//                        .join(itemReport.item, item)
//                        .where(itemReport.id.eq(itemReportId))
//                        .fetchOne()
//        );
//        Item itemEntity = optionalItem.orElseThrow(ItemNotFoundException::new);
//
//        List<ItemImgResDto> imgList = itemImgRepository.findAllByItemId(itemEntity.getId())
//                .stream()
//                .map(ItemImgResDto::of)
//                .toList();
//
//        List<ItemLinkResDto> linkList = itemLinkRepository.findAllByItemId(itemEntity.getId())
//                .stream()
//                .map(ItemLinkResDto::of)
//                .toList();
//
//        ItemReportDetailDto detailDto = jpaQueryFactory
//                .select(Projections.constructor(ItemReportDetailDto.class,
//                        itemReport.reporter.id,
//                        itemReport.reporter.nickname,
//                        item.user.id,
//                        item.user.nickname,
//                        itemReport.id,
//                        itemReport.itemReportReason,
//                        itemReport.content,
//                        itemReport.reportStatus,
//                        Expressions.constant(imgList),
//                        Expressions.constant(linkList),
//                        Expressions.constant(CelebSearchResDto.of(itemEntity.getCeleb())),
//                        Expressions.constant(BrandSearchResDto.of(itemEntity.getBrand())),
//                        Expressions.constant(ItemCategoryDto.of(itemEntity.getCategory())),
//                        item.additionalInfo,
//                        item.color,
//                        item.name,
//                        item.price,
//                        item.whenDiscovery,
//                        item.whereDiscovery,
//                        itemReport.createdAt,
//                        itemReport.updatedAt
//                ))
//                .from(itemReport)
//                .join(itemReport.item, item)
//                .join(itemReport.reporter, reporterUser)
//                .join(itemReport.item.user, reportedUser)
//                .where(itemReport.id.eq(itemReportId))
//                .fetchOne();

        ItemReport content = jpaQueryFactory.selectFrom(itemReport)
                .leftJoin(itemReport.item, item).fetchJoin()
                .leftJoin(item).on(itemImg.item.eq(item))
                .leftJoin(item).on(itemLink.item.eq(item))
                .leftJoin(item.brand, brand).fetchJoin()
                .leftJoin(item.celeb, celeb).fetchJoin()
                .leftJoin(item.category, itemCategory).fetchJoin()
                .where(itemReport.id.eq(itemReportId))
                .fetchOne();

        return content;
    }

}
