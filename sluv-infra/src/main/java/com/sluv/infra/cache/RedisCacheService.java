package com.sluv.infra.cache;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisCacheService<T> implements CacheService<T> {

    private final RedisTemplate<String, T> redisStringDataTemplate;

    @Async(value = "redisThreadPoolExecutor")
    @Override
    public void saveWithKey(String key, T data) {
        ValueOperations<String, T> dataOperations = redisStringDataTemplate.opsForValue();
        dataOperations.set(key, data, 1, TimeUnit.DAYS);
    }

    @Override
    public T findByKey(String key) {
        ValueOperations<String, T> dataOperations = redisStringDataTemplate.opsForValue();
        return dataOperations.get(key);
    }

    @Async(value = "redisThreadPoolExecutor")
    @Override
    public void deleteByKey(String key) {
        ValueOperations<String, T> dataOperations = redisStringDataTemplate.opsForValue();
        dataOperations.getAndDelete(key);
    }

}
