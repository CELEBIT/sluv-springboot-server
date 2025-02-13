package com.sluv.infra.cache;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisItemCacheService<T> implements ItemCacheService<T> {

    private final RedisTemplate<String, T> redisStringDataTemplate;

    @Async(value = "redisThreadPoolExecutor")
    @Override
    public void saveWithId(Long itemId, T itemData) {
        ValueOperations<String, T> dataOperations = redisStringDataTemplate.opsForValue();
        dataOperations.set("item:" + itemId, itemData, 1, TimeUnit.HOURS);
    }

    @Override
    public T findById(Long itemId) {
        ValueOperations<String, T> dataOperations = redisStringDataTemplate.opsForValue();
        return dataOperations.get("item:" + itemId);
    }

    @Async(value = "redisThreadPoolExecutor")
    @Override
    public void deleteById(Long itemId) {
        ValueOperations<String, T> dataOperations = redisStringDataTemplate.opsForValue();
        dataOperations.getAndDelete("item:" + itemId);
    }

}
