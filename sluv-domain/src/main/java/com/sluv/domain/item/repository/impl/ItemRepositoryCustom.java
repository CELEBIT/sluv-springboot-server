package com.sluv.domain.item.repository.impl;

import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.item.dto.ItemWithCountDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.enums.ItemStatus;
import com.sluv.domain.search.dto.SearchFilterReqDto;
import com.sluv.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {
    List<String> findTopPlace();

    List<Item> findSameCelebItem(Long itemId, Long celebId, boolean celebJudge, List<Long> blockUserIds);

    List<Item> findSameBrandItem(Long itemId, Long brandId, boolean brandJudge, List<Long> blockUserIds);

    Page<Item> getRecentItem(User user, Pageable pageable);

    Page<Item> getAllScrapItem(User user, Pageable pageable);

    Page<ItemSimpleDto> getClosetItems(Closet closet, Pageable pageable);

    List<Item> getSameClosetItems(Long itemId, List<Closet> closetList, List<Long> blockUserIds);

    Page<Item> getSearchItem(List<Long> itemIdList, SearchFilterReqDto dto, Pageable pageable);

    Page<Item> getRecommendItemPage(List<Long> blockUserIds, Pageable pageable);

    Long getSearchItemCount(List<Long> itemIdList, SearchFilterReqDto dto);

    List<Item> getRecentTop2Item(User targetUser);

    Page<Item> getUserAllItem(Long userId, Pageable pageable);

    Page<Item> getAllByUserLikeItem(User user, List<Long> blockUserIds, Pageable pageable);

    Page<Item> getCelebSummerItem(Pageable pageable, SearchFilterReqDto dto);

    Page<Item> getNowBuyItem(List<Long> blockUserIds, Pageable pageable, SearchFilterReqDto dto);

    Page<Item> getNewItem(List<Long> blockUserIds, Pageable pageable);

    Page<Item> getLuxuryItem(List<Long> blockUserIds, Pageable pageable, SearchFilterReqDto dto);

    List<Item> getItemByOverPrice(int price);

    Page<Item> getEfficientItem(List<Long> blockUserIds, Pageable pageable, SearchFilterReqDto filterReqDto);

    List<Item> getItemByUnderPrice(int price);

    List<Item> getPreviousWeekHotItem();

    List<Item> getWeekHotItem();

    List<Item> getDayHotItem();

    List<Item> getYesterdayHotItem();

    Page<Item> getHotCelebItem(Long celebId, Pageable pageable, SearchFilterReqDto dto);

    List<Item> getCurationItem(User user, List<Celeb> interestedCeleb, List<Long> blockUserIds);

    List<Item> getHowAboutItem(User user, List<Celeb> interestedCeleb);

    List<ItemSimpleDto> getItemSimpleDto(User user, List<Item> items);

    Page<Item> getUserAllRecentItem(User user, List<Long> blockUserIds, Pageable pageable);

    Item getItemByIdWithCelebAndBrand(Long itemId);

    List<Item> getSearchItemIds(String word);

    List<Item> getItemContainKeyword(String keyword);

    Page<Item> getTrendItems(List<Long> blockUserIds, Pageable pageable);

    List<Item> getAllItemWithCelebCategory();

    List<ItemWithCountDto> getTop3HotItem();

    void changeAllNewBrandToBrand(Brand brand, Long newBrandId);

    void changeAllNewCelebToCeleb(Celeb celeb, Long newCelebId);

    List<Item> getAllByItemStatus(List<Long> blockUserIds, ItemStatus itemStatus);
}
