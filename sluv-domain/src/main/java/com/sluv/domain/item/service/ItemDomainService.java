package com.sluv.domain.item.service;

import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.item.dto.ItemSimpleDto;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemDomainService {

    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public Item findById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<Item> getAllByUserLikeItem(User user, Pageable pageable) {
        return itemRepository.getAllByUserLikeItem(user, pageable);
    }

    @Transactional(readOnly = true)
    public List<ItemSimpleDto> getItemSimpleDto(User user, List<Item> content) {
        return itemRepository.getItemSimpleDto(user, content);
    }

    @Transactional(readOnly = true)
    public Page<Item> getUserAllRecentItem(User user, Pageable pageable) {
        return itemRepository.getUserAllRecentItem(user, pageable);
    }

    @Transactional(readOnly = true)
    public List<Item> getRecentTop2Item(User targetUser) {
        return itemRepository.getRecentTop2Item(targetUser);
    }

    @Transactional(readOnly = true)
    public Long countByUserIdAndItemStatus(Long userId, ItemStatus itemStatus) {
        return itemRepository.countByUserIdAndItemStatus(userId, itemStatus);
    }

    @Transactional(readOnly = true)
    public Page<Item> getUserAllItem(Long userId, Pageable pageable) {
        return itemRepository.getUserAllItem(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ItemSimpleDto> getClosetItems(Closet closet, Pageable pageable) {
        return itemRepository.getClosetItems(closet, pageable);
    }

    @Transactional(readOnly = true)
    public List<Item> findAllByUserId(Long userId) {
        return itemRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<String> findTopPlace() {
        return itemRepository.findTopPlace();
    }

    @Transactional(readOnly = true)
    public Page<Item> getRecentItem(User user, Pageable pageable) {
        return itemRepository.getRecentItem(user, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Item> getAllScrapItem(User user, Pageable pageable) {
        return itemRepository.getAllScrapItem(user, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Item> getRecommendItemPage(Pageable pageable) {
        return itemRepository.getRecommendItemPage(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Item> getNewItem(Pageable pageable) {
        return itemRepository.getNewItem(pageable);
    }

    @Transactional(readOnly = true)
    public List<Item> getWeekHotItem() {
        return itemRepository.getWeekHotItem();
    }

    @Transactional(readOnly = true)
    public List<Item> getDayHotItem() {
        return itemRepository.getDayHotItem();
    }

    @Transactional(readOnly = true)
    public List<Item> getCurationItem(User user, List<Celeb> interestedCeleb) {
        return itemRepository.getCurationItem(user, interestedCeleb);
    }

    @Transactional(readOnly = true)
    public List<Item> findAllByItemStatus(ItemStatus itemStatus) {
        return itemRepository.findAllByItemStatus(itemStatus);
    }

    @Transactional(readOnly = true)
    public Item getItemByIdWithCelebAndBrand(Long itemId) {
        return itemRepository.getItemByIdWithCelebAndBrand(itemId);
    }

    @Transactional(readOnly = true)
    public List<Item> findSameCelebItem(Long itemId, Long celebId, boolean flag) {
        return itemRepository.findSameCelebItem(itemId, celebId, flag);
    }

    @Transactional(readOnly = true)
    public List<Item> findSameBrandItem(Long itemId, Long brandId, boolean flag) {
        return itemRepository.findSameBrandItem(itemId, brandId, flag);
    }

    @Transactional(readOnly = true)
    public List<Item> getSameClosetItems(Long itemId, List<Closet> recentAddClosets) {
        return itemRepository.getSameClosetItems(itemId, recentAddClosets);
    }

    @Transactional
    public Item saveItem(Item postItem) {
        return itemRepository.save(postItem);
    }

    @Transactional(readOnly = true)
    public Page<Item> getLuxuryItem(Pageable pageable, SearchFilterReqDto dto) {
        return itemRepository.getLuxuryItem(pageable, dto);
    }

    @Transactional(readOnly = true)
    public Page<Item> getEfficientItem(Pageable pageable, SearchFilterReqDto filterReqDto) {
        return itemRepository.getEfficientItem(pageable, filterReqDto);
    }

    @Transactional(readOnly = true)
    public Page<Item> getHotCelebItem(Long celebId, Pageable pageable, SearchFilterReqDto dto) {
        return itemRepository.getHotCelebItem(celebId, pageable, dto);
    }

    @Transactional(readOnly = true)
    public Page<Item> getNowBuyItem(Pageable pageable, SearchFilterReqDto dto) {
        return itemRepository.getNowBuyItem(pageable, dto);
    }

    @Transactional(readOnly = true)
    public Page<Item> getCelebSummerItem(Pageable pageable, SearchFilterReqDto dto) {
        return itemRepository.getCelebSummerItem(pageable, dto);
    }

    @Transactional(readOnly = true)
    public Page<Item> getSearchItem(List<Long> searchItemIds, SearchFilterReqDto dto, Pageable pageable) {
        return itemRepository.getSearchItem(searchItemIds, dto, pageable);
    }

    @Transactional(readOnly = true)
    public Long getSearchItemCount(List<Long> itemIds, SearchFilterReqDto dto) {
        return itemRepository.getSearchItemCount(itemIds, dto);
    }

    @Transactional(readOnly = true)
    public List<Item> getItemContainKeyword(String keyword) {
        return itemRepository.getItemContainKeyword(keyword);
    }

    @Transactional(readOnly = true)
    public List<Item> getItemByOverPrice(int price) {
        return itemRepository.getItemByOverPrice(price);
    }

    @Transactional(readOnly = true)
    public List<Item> getItemByUnderPrice(int price) {
        return itemRepository.getItemByUnderPrice(price);
    }

    @Transactional(readOnly = true)
    public List<Item> getPreviousWeekHotItem() {
        return itemRepository.getPreviousWeekHotItem();
    }

    @Transactional(readOnly = true)
    public List<Item> getYesterdayHotItem() {
        return itemRepository.getYesterdayHotItem();
    }

    @Transactional(readOnly = true)
    public Page<Item> getTrendItems(Pageable pageable) {
        return itemRepository.getTrendItems(pageable);
    }

}
