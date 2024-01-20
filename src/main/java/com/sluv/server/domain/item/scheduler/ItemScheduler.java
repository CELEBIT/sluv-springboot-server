package com.sluv.server.domain.item.scheduler;

import com.sluv.server.domain.item.entity.DayHotItem;
import com.sluv.server.domain.item.entity.EfficientItem;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.LuxuryItem;
import com.sluv.server.domain.item.entity.WeekHotItem;
import com.sluv.server.domain.item.repository.DayHotItemRepository;
import com.sluv.server.domain.item.repository.EfficientItemRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.item.repository.LuxuryItemRepository;
import com.sluv.server.domain.item.repository.WeekHotItemRepository;
import java.util.Calendar;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ItemScheduler {

    private final LuxuryItemRepository luxuryItemRepository;
    private final EfficientItemRepository efficientItemRepository;
    private final WeekHotItemRepository weekHotItemRepository;
    private final DayHotItemRepository dayHotItemRepository;
    private final ItemRepository itemRepository;

    /**
     * 럭셔리 아이템 업데이트
     */
    @Scheduled(cron = "0 0 0 * * *") // 초 분 시 일 월 요일
    public void updateLuxuryItem() {
        log.info("LuxuryItem Update Time: {}", Calendar.getInstance().getTime());

        log.info("Delete Old LuxuryItem Data");
        luxuryItemRepository.deleteAll();

        log.info("Get LuxuryItem. Time: {}", Calendar.getInstance().getTime());
        List<Item> newLuxuryItem = itemRepository.updateLuxuryItem();

        log.info("Save LuxuryItem. Time: {}", Calendar.getInstance().getTime());

        newLuxuryItem.forEach(item ->
                luxuryItemRepository.save(
                        LuxuryItem.toEntity(item)
                )
        );
    }

    /**
     * 가성비 선물 아이템 업데이트
     */
    @Scheduled(cron = "0 0 0 * * *") // 초 분 시 일 월 요일
    public void updateEfficientItem() {
        log.info("EfficientItem Update Time: {}", Calendar.getInstance().getTime());

        log.info("Delete Old EfficientItem Data");
        efficientItemRepository.deleteAll();

        log.info("Get EfficientItem. Time: {}", Calendar.getInstance().getTime());
        List<Item> newEfficientItem = itemRepository.updateEfficientItem();

        log.info("Save EfficientItem. Time: {}", Calendar.getInstance().getTime());

        newEfficientItem.forEach(item ->
                efficientItemRepository.save(
                        EfficientItem.toEntity(item)
                )
        );
    }

    /**
     * 주간 HOT 셀럽 아이템
     */
    @Scheduled(cron = "0 0 0 * * MON") // 초 분 시 일 월 요일
    public void updateWeekHotItem() {
        log.info("WeekHotItem Update Time: {}", Calendar.getInstance().getTime());

        log.info("Delete Old WeekHotItem Data");
        weekHotItemRepository.deleteAll();

        log.info("Get WeekHotItem. Time: {}", Calendar.getInstance().getTime());
        List<Item> newWeekHotItem = itemRepository.updateWeekHotItem();

        log.info("Save WeekHotItem. Time: {}", Calendar.getInstance().getTime());

        newWeekHotItem.forEach(item ->
                weekHotItemRepository.save(
                        WeekHotItem.toEntity(item)
                )
        );
    }

    /**
     * 일간 HOT 셀럽 아이템
     */
    @Scheduled(cron = "0 0 0 * * *") // 초 분 시 일 월 요일
    public void updateDayHotItem() {
        log.info("DayHotItem Update Time: {}", Calendar.getInstance().getTime());

        log.info("Delete Old DayHotItem Data");
        dayHotItemRepository.deleteAll();

        log.info("Get DayHotItem. Time: {}", Calendar.getInstance().getTime());
        List<Item> newDayHotItem = itemRepository.updateDayHotItem();

        log.info("Save DayHotItem. Time: {}", Calendar.getInstance().getTime());

        newDayHotItem.forEach(item ->
                dayHotItemRepository.save(
                        DayHotItem.toEntity(item)
                )
        );
    }
}
