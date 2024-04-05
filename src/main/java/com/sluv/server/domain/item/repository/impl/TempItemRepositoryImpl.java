package com.sluv.server.domain.item.repository.impl;

import static com.sluv.server.domain.brand.entity.QBrand.brand;
import static com.sluv.server.domain.brand.entity.QNewBrand.newBrand;
import static com.sluv.server.domain.celeb.entity.QCeleb.celeb;
import static com.sluv.server.domain.celeb.entity.QCelebCategory.celebCategory;
import static com.sluv.server.domain.celeb.entity.QNewCeleb.newCeleb;
import static com.sluv.server.domain.item.entity.QItemCategory.itemCategory;
import static com.sluv.server.domain.item.entity.QTempItem.tempItem;
import static com.sluv.server.domain.item.entity.QTempItemImg.tempItemImg;
import static com.sluv.server.domain.item.entity.QTempItemLink.tempItemLink;
import static com.sluv.server.domain.item.entity.hashtag.QHashtag.hashtag;
import static com.sluv.server.domain.item.entity.hashtag.QTempItemHashtag.tempItemHashtag;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.celeb.entity.QCeleb;
import com.sluv.server.domain.celeb.entity.QCelebCategory;
import com.sluv.server.domain.item.dto.ItemHashtagResponseDto;
import com.sluv.server.domain.item.dto.ItemImgResDto;
import com.sluv.server.domain.item.dto.ItemLinkResDto;
import com.sluv.server.domain.item.dto.TempItemResDto;
import com.sluv.server.domain.item.entity.QItemCategory;
import com.sluv.server.domain.item.entity.TempItem;
import com.sluv.server.domain.item.entity.TempItemImg;
import com.sluv.server.domain.item.entity.TempItemLink;
import com.sluv.server.domain.item.entity.hashtag.Hashtag;
import com.sluv.server.domain.item.entity.hashtag.TempItemHashtag;
import com.sluv.server.domain.user.entity.User;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    @Override
    public List<TempItemResDto> getTempItemResDto(List<TempItem> content) {
        List<Long> tempItemIds = content.stream().map(TempItem::getId).toList();
        List<TempItemImg> tempItemImgs = jpaQueryFactory.selectFrom(tempItemImg)
                .where(tempItemImg.tempItem.id.in(tempItemIds)).fetch();

        List<TempItemLink> tempItemLinks = jpaQueryFactory.selectFrom(tempItemLink)
                .where(tempItemLink.tempItem.id.in(tempItemIds)).fetch();

        List<TempItemHashtag> tempItemHashtags = jpaQueryFactory.selectFrom(tempItemHashtag)
                .leftJoin(tempItemHashtag.hashtag, hashtag).fetchJoin()
                .where(tempItemHashtag.tempItem.id.in(tempItemIds)).fetch();

        Map<Long, List<ItemImgResDto>> tempItemImgMap = tempItemImgs.stream().collect(
                Collectors.groupingBy(ti -> ti.getTempItem().getId(),
                        Collectors.mapping(ItemImgResDto::of, Collectors.toList())));

        Map<Long, List<ItemLinkResDto>> tempItemLinkMap = tempItemLinks.stream().collect(
                Collectors.groupingBy(tl -> tl.getTempItem().getId(),
                        Collectors.mapping(ItemLinkResDto::of, Collectors.toList())));

        Map<Long, List<Hashtag>> tempItemHashtagMap = tempItemHashtags.stream().collect(
                Collectors.groupingBy(th -> th.getTempItem().getId(),
                        Collectors.mapping(TempItemHashtag::getHashtag, Collectors.toList())));

        return content.stream().map(ti -> {
            List<ItemHashtagResponseDto> hashtags = null;
            if (tempItemHashtagMap.get(ti.getId()) != null) {
                hashtags = tempItemHashtagMap.get(ti.getId()).stream()
                        .map(ItemHashtagResponseDto::of).toList();
            }
            return TempItemResDto.of(ti, tempItemImgMap.get(ti.getId()), tempItemLinkMap.get(ti.getId()), hashtags);
        }).toList();
    }
}

