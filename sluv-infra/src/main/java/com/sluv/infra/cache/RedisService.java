package com.sluv.infra.cache;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService implements CacheService {

    private final RedisTemplate<String, Long> redisStringLongTemplate;
    private final RedisTemplate<String, Object> redisStringItemDetailFixDataTemplate;
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
    public void saveItemDetailFixData(Long itemId, Object itemDetailFixData) {
        ValueOperations<String, Object> itemDetailFixDataCache = redisStringItemDetailFixDataTemplate.opsForValue();
        itemDetailFixDataCache.set("item:" + itemId, itemDetailFixData, 1, TimeUnit.HOURS);
    }

    @Override
    public Object findItemDetailFixDataByItemId(Long itemId) {
        ValueOperations<String, Object> itemDetailFixDataCache = redisStringItemDetailFixDataTemplate.opsForValue();
        return itemDetailFixDataCache.get("item:" + itemId);
    }

    @Async(value = "redisThreadPoolExecutor")
    @Override
    public void deleteItemDetailFixDataByItemId(Long itemId) {
        ValueOperations<String, Object> itemDetailFixDataCache = redisStringItemDetailFixDataTemplate.opsForValue();
        itemDetailFixDataCache.getAndDelete("item:" + itemId);
    }

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

    private Date getNextDate() {
        Calendar calendar = Calendar.getInstance();

        // 다음날 00시 00분 00초로 설정.
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

}
