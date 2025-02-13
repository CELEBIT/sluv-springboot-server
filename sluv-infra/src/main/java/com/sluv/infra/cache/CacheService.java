package com.sluv.infra.cache;

import org.springframework.stereotype.Service;

@Service
public interface CacheService<T> {
    void saveWithKey(String key, T data);

    T findByKey(String key);

    void deleteByKey(String key);
}
