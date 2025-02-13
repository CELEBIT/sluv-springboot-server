package com.sluv.infra.counter.view;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisViewCounter implements ViewCounter {

    private final RedisTemplate<String, Long> redisStringLongTemplate;

    @Async(value = "redisThreadPoolExecutor")
    @Override
    public void saveUserViewItemId(Long userId, Long itemId) {
        ValueOperations<String, Long> userViewItemCache = redisStringLongTemplate.opsForValue();
        userViewItemCache.set("userItem:" + userId + "::" + itemId, 0L, 1, TimeUnit.HOURS);
    }

    @Override
    public boolean existUserViewItemId(Long userId, Long itemId) {
        ValueOperations<String, Long> userViewItemCache = redisStringLongTemplate.opsForValue();
        Long isExist = userViewItemCache.get("userItem:" + userId + "::" + itemId);
        return isExist != null;
    }

    @Async(value = "redisThreadPoolExecutor")
    @Override
    public void saveUserViewQuestionId(Long userId, Long questionId) {
        ValueOperations<String, Long> userViewQuestionCache = redisStringLongTemplate.opsForValue();
        userViewQuestionCache.set("userQuestion:" + userId + "::" + questionId, 0L, 1, TimeUnit.HOURS);
    }

    @Override
    public boolean existUserViewQuestionId(Long userId, Long questionId) {
        ValueOperations<String, Long> userViewQuestionCache = redisStringLongTemplate.opsForValue();
        Long isExist = userViewQuestionCache.get("userQuestion:" + userId + "::" + questionId);
        return isExist != null;
    }


}
