package com.sluv.server.global.cache;


import com.sluv.server.domain.item.dto.ItemDetailFixData;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService implements CacheService {

    private final RedisTemplate<String, Long> redisStringLongTemplate;
    private final RedisTemplate<String, ItemDetailFixData> redisStringItemDetailFixDataTemplate;
    private final String VISITANT_KEY = "visitant";

    @Async(value = "redisThreadPoolExecutor")
    @Override
    public void visitMember(Long memberId) {
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

    @Async(value = "redisThreadPoolExecutor")
    @Override
    public void saveItemDetailFixData(Long itemId, ItemDetailFixData itemDetailFixData) {
        ValueOperations<String, ItemDetailFixData> itemDetailFixDataCache = redisStringItemDetailFixDataTemplate.opsForValue();
        itemDetailFixDataCache.set("item:" + itemId, itemDetailFixData, 1, TimeUnit.HOURS);
    }

    @Override
    public ItemDetailFixData findItemDetailFixDataByItemId(Long itemId) {
        ValueOperations<String, ItemDetailFixData> itemDetailFixDataCache = redisStringItemDetailFixDataTemplate.opsForValue();
        return itemDetailFixDataCache.get("item:" + itemId);
    }

    @Async(value = "redisThreadPoolExecutor")
    @Override
    public void deleteItemDetailFixDataByItemId(Long itemId) {
        ValueOperations<String, ItemDetailFixData> itemDetailFixDataCache = redisStringItemDetailFixDataTemplate.opsForValue();
        itemDetailFixDataCache.getAndDelete("item:" + itemId);
    }

    @Async(value = "redisThreadPoolExecutor")
    @Override
    public void saveUserViewItemId(Long userId, Long itemId) {
        ValueOperations<String, Long> userViewItemCache = redisStringLongTemplate.opsForValue();
        userViewItemCache.set("userItem:" + userId, itemId, 20, TimeUnit.MINUTES);
    }
}
