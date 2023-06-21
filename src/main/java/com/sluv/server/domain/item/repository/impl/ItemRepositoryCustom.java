package com.sluv.server.domain.item.repository.impl;

import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.search.dto.SearchFilterReqDto;
import com.sluv.server.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {
    List<String> findTopPlace();
    List<Item> findSameCelebItem(Item item, boolean celebJudge);

    List<Item>  findSameBrandItem(Item item, boolean brandJudge);

    Page<Item> getRecentItem(User user, Pageable pageable);

    Page<Item> getAllScrapItem(User user, Pageable pageable);

    Page<Item> getClosetItems(Closet closet, Pageable pageable);

    List<Item> getSameClosetItems(Item item, List<Closet> closetList);

    Page<Item> getSearchItem(List<Long> itemIdList, SearchFilterReqDto dto, Pageable pageable);

    Page<Item> getRecommendItemPage(Pageable pageable);

    Long getSearchItemCount(List<Long> itemIdList, SearchFilterReqDto dto);

    List<Item> getRecentTop2Item(User targetUser);

    Page<Item> getUserAllItem(Long userId, Pageable pageable);
}
