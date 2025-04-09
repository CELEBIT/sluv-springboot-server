package com.sluv.domain.item.service;

import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.item.dto.ItemWithCountDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.enums.ItemStatus;
import com.sluv.domain.item.exception.ItemNotFoundException;
import com.sluv.domain.item.repository.ItemRepository;
import com.sluv.domain.search.dto.SearchFilterReqDto;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemDomainService {

    private final ItemRepository itemRepository;

    public Item findById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
    }

    public Page<Item> getAllByUserLikeItem(User user, List<Long> blockUserIds, Pageable pageable) {
        return itemRepository.getAllByUserLikeItem(user, blockUserIds, pageable);
    }

    public List<ItemSimpleDto> getItemSimpleDto(User user, List<Item> content) {
        return itemRepository.getItemSimpleDto(user, content);
    }

    public Page<Item> getUserAllRecentItem(User user, List<Long> blockUserIds, Pageable pageable) {
        return itemRepository.getUserAllRecentItem(user, blockUserIds, pageable);
    }

    public List<Item> getRecentTop2Item(User targetUser) {
        return itemRepository.getRecentTop2Item(targetUser);
    }

    public Long countByUserIdAndItemStatus(Long userId, ItemStatus itemStatus) {
        return itemRepository.countByUserIdAndItemStatus(userId, itemStatus);
    }

    public Page<Item> getUserAllItem(Long userId, Pageable pageable) {
        return itemRepository.getUserAllItem(userId, pageable);
    }

    public Page<ItemSimpleDto> getClosetItems(Closet closet, Pageable pageable) {
        return itemRepository.getClosetItems(closet, pageable);
    }

    public List<Item> findAllByUserId(Long userId) {
        return itemRepository.findAllByUserId(userId);
    }

    public List<String> findTopPlace() {
        return itemRepository.findTopPlace();
    }

    public Page<Item> getRecentItem(User user, Pageable pageable) {
        return itemRepository.getRecentItem(user, pageable);
    }

    public Page<Item> getAllScrapItem(User user, Pageable pageable) {
        return itemRepository.getAllScrapItem(user, pageable);
    }

    public Page<Item> getRecommendItemPage(List<Long> blockUserIds, Pageable pageable) {
        return itemRepository.getRecommendItemPage(blockUserIds, pageable);
    }

    public Page<Item> getNewItem(List<Long> blockUserIds, Pageable pageable) {
        return itemRepository.getNewItem(blockUserIds, pageable);
    }

    public List<Item> getWeekHotItem() {
        return itemRepository.getWeekHotItem();
    }

    public List<Item> getDayHotItem() {
        return itemRepository.getDayHotItem();
    }

    public List<Item> getCurationItem(User user, List<Celeb> interestedCeleb, List<Long> blockUserIds) {
        return itemRepository.getCurationItem(user, interestedCeleb, blockUserIds);
    }

    public List<Item> getAllByItemStatus(List<Long> blockUserIds, ItemStatus itemStatus) {
        return itemRepository.getAllByItemStatus(blockUserIds, itemStatus);
    }

    public Item getItemByIdWithCelebAndBrand(Long itemId) {
        return itemRepository.getItemByIdWithCelebAndBrand(itemId);
    }

    public List<Item> findSameCelebItem(Long itemId, Long celebId, boolean flag) {
        return itemRepository.findSameCelebItem(itemId, celebId, flag);
    }

    public List<Item> findSameBrandItem(Long itemId, Long brandId, boolean flag) {
        return itemRepository.findSameBrandItem(itemId, brandId, flag);
    }

    public List<Item> getSameClosetItems(Long itemId, List<Closet> recentAddClosets, List<Long> blockUserIds) {
        return itemRepository.getSameClosetItems(itemId, recentAddClosets, blockUserIds);
    }

    public Item saveItem(Item postItem) {
        return itemRepository.save(postItem);
    }

    public Page<Item> getLuxuryItem(List<Long> blockUserIds, Pageable pageable, SearchFilterReqDto dto) {
        return itemRepository.getLuxuryItem(blockUserIds, pageable, dto);
    }

    public Page<Item> getEfficientItem(List<Long> blockUserIds, Pageable pageable, SearchFilterReqDto filterReqDto) {
        return itemRepository.getEfficientItem(blockUserIds, pageable, filterReqDto);
    }

    public Page<Item> getHotCelebItem(Long celebId, Pageable pageable, SearchFilterReqDto dto) {
        return itemRepository.getHotCelebItem(celebId, pageable, dto);
    }

    public Page<Item> getNowBuyItem(List<Long> blockUserIds, Pageable pageable, SearchFilterReqDto dto) {
        return itemRepository.getNowBuyItem(blockUserIds, pageable, dto);
    }

    public Page<Item> getCelebSummerItem(Pageable pageable, SearchFilterReqDto dto) {
        return itemRepository.getCelebSummerItem(pageable, dto);
    }

    public Page<Item> getSearchItem(List<Long> searchItemIds, SearchFilterReqDto dto, Pageable pageable) {
        return itemRepository.getSearchItem(searchItemIds, dto, pageable);
    }

    public Long getSearchItemCount(List<Long> itemIds, SearchFilterReqDto dto) {
        return itemRepository.getSearchItemCount(itemIds, dto);
    }

    public List<Item> getItemContainKeyword(String keyword) {
        return itemRepository.getItemContainKeyword(keyword);
    }

    public List<Item> getItemByOverPrice(int price) {
        return itemRepository.getItemByOverPrice(price);
    }

    public List<Item> getItemByUnderPrice(int price) {
        return itemRepository.getItemByUnderPrice(price);
    }

    public List<Item> getPreviousWeekHotItem() {
        return itemRepository.getPreviousWeekHotItem();
    }

    public List<Item> getYesterdayHotItem() {
        return itemRepository.getYesterdayHotItem();
    }

    public Page<Item> getTrendItems(List<Long> blockUserIds, Pageable pageable) {
        return itemRepository.getTrendItems(blockUserIds, pageable);
    }

    public List<Item> getAllItemWithCelebCategory() {
        return itemRepository.getAllItemWithCelebCategory();
    }

    public List<ItemWithCountDto> getTop3HotItem() {
        return itemRepository.getTop3HotItem();
    }

    public void changeAllNewBrandToBrand(Brand brand, Long newBrandId) {
        itemRepository.changeAllNewBrandToBrand(brand, newBrandId);
    }

    public void changeAllNewCelebToCeleb(Celeb celeb, Long newCelebId) {
        itemRepository.changeAllNewCelebToCeleb(celeb, newCelebId);
    }

}
