package com.sluv.infra.cache;

import org.springframework.stereotype.Service;

@Service
public interface ItemCacheService<T> {
    void saveWithId(Long itemId, T itemData);

    T findById(Long itemId);

    void deleteById(Long itemId);
}
