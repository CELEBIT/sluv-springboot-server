package com.sluv.api.item.service;

import com.sluv.api.item.dto.ItemDetailFixData;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.service.ItemDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemCacheService {
    private final ItemDomainService itemDomainService;
    private final com.sluv.infra.cache.ItemCacheService<ItemDetailFixData> itemCacheService;

    @Transactional
    @Async(value = "asyncThreadPoolExecutor")
    public void deleteAllItemCacheByUserId(Long userId) {

        List<Long> userItemIds = itemDomainService.findAllByUserId(userId).stream()
                .map(Item::getId)
                .toList();

        userItemIds.forEach(itemCacheService::deleteById);
    }

}
