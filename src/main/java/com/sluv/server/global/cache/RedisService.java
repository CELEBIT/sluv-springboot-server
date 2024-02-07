package com.sluv.server.global.cache;


import com.sluv.server.domain.item.dto.ItemDetailFixData;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
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

    @Override
    public long saveUserViewItemId(Long userId, Long itemId) {
        SetOperations<String, Long> userViewItemCache = redisStringLongTemplate.opsForSet();
        Long addStatus = Optional.ofNullable(userViewItemCache.add("userItem:" + userId, itemId)).orElse(0L);
        Date nextDate = getNextDate();
        redisStringLongTemplate.expireAt("userItem:" + userId, nextDate);

        return addStatus;
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

    @Override
    public long saveUserViewQuestionId(Long userId, Long questionId) {
        SetOperations<String, Long> userViewQuestionCache = redisStringLongTemplate.opsForSet();
        Long addStatus = Optional.ofNullable(userViewQuestionCache.add("userQuestion:" + userId, questionId))
                .orElse(0L);
        Date nextDate = getNextDate();
        redisStringLongTemplate.expireAt("userQuestion:" + userId, nextDate);

        return addStatus;
    }

}
