package com.sluv.server.domain.item.service;

import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.global.cache.CacheService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemCacheService {
    private final ItemRepository itemRepository;
    private final CacheService cacheService;

    @Async(value = "asyncThreadPoolExecutor")
    public void deleteAllItemCacheByUserId(Long userId) {

        List<Long> userItemIds = itemRepository.findAllByUserId(userId).stream()
                .map(Item::getId)
                .toList();

        userItemIds.forEach(cacheService::deleteItemDetailFixDataByItemId);
    }
}
