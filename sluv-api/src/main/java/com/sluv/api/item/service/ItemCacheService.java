package com.sluv.api.item.service;

import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.infra.cache.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemCacheService {
    private final ItemDomainService itemDomainService;
    private final CacheService cacheService;

    @Transactional
    @Async(value = "asyncThreadPoolExecutor")
    public void deleteAllItemCacheByUserId(Long userId) {

        List<Long> userItemIds = itemDomainService.findAllByUserId(userId).stream()
                .map(Item::getId)
                .toList();

        userItemIds.forEach(cacheService::deleteItemDetailFixDataByItemId);
    }

}