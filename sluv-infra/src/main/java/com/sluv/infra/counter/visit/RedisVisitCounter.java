package com.sluv.infra.counter.visit;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisVisitCounter implements VisitCounter {

    private final RedisTemplate<String, Long> redisStringLongTemplate;
    private final String VISITANT_KEY = "visitant";

    @Async(value = "redisThreadPoolExecutor")
    @Override
    public void countVisit(Long memberId) {
        SetOperations<String, Long> visitantLog = redisStringLongTemplate.opsForSet();
        visitantLog.add(VISITANT_KEY, memberId);
    }

    @Override
    public Long getVisitantCount() {
        SetOperations<String, Long> visitantLog = redisStringLongTemplate.opsForSet();
        return visitantLog.size(VISITANT_KEY);
    }

    @Override
    public void clearVisitantCount() {
        redisStringLongTemplate.delete(VISITANT_KEY);
    }


}
