package com.sluv.server.global.cache;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService implements CacheService {

    private final RedisTemplate<String, Long> redisTemplate;
    private final String VISITANT_KEY = "visitant";

    @Override
    public void insert(Long memberId) {
        SetOperations<String, Long> visitantLog = redisTemplate.opsForSet();
        visitantLog.add(VISITANT_KEY, memberId);
    }

    @Override
    public Long getCount() {
        SetOperations<String, Long> visitantLog = redisTemplate.opsForSet();
        return visitantLog.size(VISITANT_KEY);
    }

    @Override
    public void clear() {
        redisTemplate.delete(VISITANT_KEY);
    }
}
