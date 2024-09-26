package com.sluv.batch.item;

import com.sluv.domain.item.entity.*;
import com.sluv.domain.item.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
@RequiredArgsConstructor
public class ItemScheduler {

    private final LuxuryItemDomainService luxuryItemDomainService;
    private final EfficientItemDomainService efficientItemDomainService;
    private final WeekHotItemDomainService weekHotItemDomainService;
    private final DayHotItemDomainService dayHotItemDomainService;
    private final ItemDomainService itemDomainService;

    /**
     * 럭셔리 아이템 업데이트
     */
    @Scheduled(cron = "0 0 0 * * *") // 초 분 시 일 월 요일
    public void updateLuxuryItem() {
        log.info("LuxuryItem Update Time: {}", Calendar.getInstance().getTime());

        log.info("Delete Old LuxuryItem Data");
        luxuryItemDomainService.deleteAllLuxuryItem();

        log.info("Get LuxuryItem. Time: {}", Calendar.getInstance().getTime());
        List<Item> newLuxuryItem = itemDomainService.getItemByOverPrice(500000);
        Collections.shuffle(newLuxuryItem, new Random());

        log.info("Save LuxuryItem. Time: {}", Calendar.getInstance().getTime());

        newLuxuryItem.subList(0, 10).forEach(item -> luxuryItemDomainService.save(LuxuryItem.toEntity(item)));
    }

    /**
     * 가성비 선물 아이템 업데이트
     */
    @Scheduled(cron = "0 0 0 * * *") // 초 분 시 일 월 요일
    public void updateEfficientItem() {
        log.info("EfficientItem Update Time: {}", Calendar.getInstance().getTime());

        log.info("Delete Old EfficientItem Data");
        efficientItemDomainService.deleteAllEfficientItem();

        log.info("Get EfficientItem. Time: {}", Calendar.getInstance().getTime());
        List<Item> newEfficientItem = itemDomainService.getItemByUnderPrice(100000);
        Collections.shuffle(newEfficientItem, new Random());

        log.info("Save EfficientItem. Time: {}", Calendar.getInstance().getTime());

        newEfficientItem.subList(0, 10).forEach(item -> efficientItemDomainService.save(EfficientItem.toEntity(item)));
    }

    /**
     * 주간 HOT 셀럽 아이템
     */
    @Scheduled(cron = "0 0 0 * * MON") // 초 분 시 일 월 요일
    public void updateWeekHotItem() {
        log.info("WeekHotItem Update Time: {}", Calendar.getInstance().getTime());

        log.info("Delete Old WeekHotItem Data");
        weekHotItemDomainService.deleteAllWeekHotItem();

        log.info("Get WeekHotItem. Time: {}", Calendar.getInstance().getTime());
        List<Item> newWeekHotItem = itemDomainService.getPreviousWeekHotItem();

        log.info("Save WeekHotItem. Time: {}", Calendar.getInstance().getTime());

        newWeekHotItem.forEach(item -> weekHotItemDomainService.save(WeekHotItem.toEntity(item)));
    }

    /**
     * 일간 HOT 셀럽 아이템
     */
    @Scheduled(cron = "0 0 0 * * *") // 초 분 시 일 월 요일
    public void updateDayHotItem() {
        log.info("DayHotItem Update Time: {}", Calendar.getInstance().getTime());

        log.info("Delete Old DayHotItem Data");
        dayHotItemDomainService.deleteAllDayHotItem();

        log.info("Get DayHotItem. Time: {}", Calendar.getInstance().getTime());
        List<Item> newDayHotItem = itemDomainService.getYesterdayHotItem();

        log.info("Save DayHotItem. Time: {}", Calendar.getInstance().getTime());

        newDayHotItem.forEach(item -> dayHotItemDomainService.save(DayHotItem.toEntity(item)));
    }
}
