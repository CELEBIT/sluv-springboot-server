package com.sluv.server.global.scheduler;

import com.querydsl.core.Tuple;
import com.sluv.server.domain.item.entity.*;
import com.sluv.server.domain.item.repository.*;
import com.sluv.server.domain.search.entity.SearchData;
import com.sluv.server.domain.search.entity.SearchRank;
import com.sluv.server.domain.search.repository.SearchDataRepository;
import com.sluv.server.domain.search.repository.SearchRankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {
    private final SearchDataRepository searchDataRepository;
    private final SearchRankRepository searchRankRepository;
    private final LuxuryItemRepository luxuryItemRepository;
    private final EfficientItemRepository efficientItemRepository;
    private final WeekHotItemRepository weekHotItemRepository;
    private final DayHotItemRepository dayHotItemRepository;
    private final ItemRepository itemRepository;

    /**
     * SearchRank 업데이트
     */
    @Scheduled(cron = "0 0 0 * * *") // 초 분 시 일 월 요일
    @Transactional
    public void updateSearchRank(){
        log.info("SearchRank Update Time: {}", Calendar.getInstance().getTime());

        log.info("Delete Old SearchRank Data");
        searchRankRepository.deleteAll();

        log.info("Get SearchData. Time: {}", Calendar.getInstance().getTime());
        List<Tuple> topData = searchDataRepository.getTopData();

        List<SearchRank> result = topData.stream().map(data ->
                SearchRank.of(
                        data.get(1, Long.class),
                        data.get(0, SearchData.class).getSearchWord()
                )
        ).toList();

        log.info("Save SearchRank. Time: {}", Calendar.getInstance().getTime());
        searchRankRepository.saveAll(result);
    }

    /**
     * 럭셔리 아이템 업데이트
     */
    @Scheduled(cron = "0 0 0 * * *") // 초 분 시 일 월 요일
    @Transactional
    public void updateLuxuryItem(){
        log.info("LuxuryItem Update Time: {}", Calendar.getInstance().getTime());

        log.info("Delete Old LuxuryItem Data");
        luxuryItemRepository.deleteAll();

        log.info("Get LuxuryItem. Time: {}", Calendar.getInstance().getTime());
        List<Item> newLuxuryItem = itemRepository.updateLuxuryItem();

        log.info("Save LuxuryItem. Time: {}", Calendar.getInstance().getTime());

        newLuxuryItem.forEach(item ->
                luxuryItemRepository.save(
                        LuxuryItem.builder()
                        .item(item)
                        .build()
                )
        );
    }

    /**
     * 가성비 선물 아이템 업데이트
     */
    @Scheduled(cron = "0 0 0 * * *") // 초 분 시 일 월 요일
    @Transactional
    public void updateEfficientItem(){
        log.info("EfficientItem Update Time: {}", Calendar.getInstance().getTime());

        log.info("Delete Old EfficientItem Data");
        efficientItemRepository.deleteAll();

        log.info("Get EfficientItem. Time: {}", Calendar.getInstance().getTime());
        List<Item> newEfficientItem = itemRepository.updateEfficientItem();

        log.info("Save EfficientItem. Time: {}", Calendar.getInstance().getTime());

        newEfficientItem.forEach(item ->
                efficientItemRepository.save(
                        EfficientItem.builder()
                                .item(item)
                                .build()
                )
        );
    }

    /**
     * 주간 HOT 셀럽 아이템
     */
    @Scheduled(cron = "0 0 0 * * MON") // 초 분 시 일 월 요일
    @Transactional
    public void updateWeekHotItem(){
        log.info("WeekHotItem Update Time: {}", Calendar.getInstance().getTime());

        log.info("Delete Old WeekHotItem Data");
        weekHotItemRepository.deleteAll();

        log.info("Get WeekHotItem. Time: {}", Calendar.getInstance().getTime());
        List<Item> newWeekHotItem = itemRepository.updateWeekHotItem();

        log.info("Save WeekHotItem. Time: {}", Calendar.getInstance().getTime());

        newWeekHotItem.forEach(item ->
                weekHotItemRepository.save(
                        WeekHotItem.builder()
                                .item(item)
                                .build()
                )
        );
    }

    /**
     * 일간 HOT 셀럽 아이템
     */
    @Scheduled(cron = "0 0 0 * * *") // 초 분 시 일 월 요일
    @Transactional
    public void updateDayHotItem(){
        log.info("DayHotItem Update Time: {}", Calendar.getInstance().getTime());

        log.info("Delete Old DayHotItem Data");
        dayHotItemRepository.deleteAll();

        log.info("Get DayHotItem. Time: {}", Calendar.getInstance().getTime());
        List<Item> newDayHotItem = itemRepository.updateDayHotItem();

        log.info("Save DayHotItem. Time: {}", Calendar.getInstance().getTime());

        newDayHotItem.forEach(item ->
                dayHotItemRepository.save(
                        DayHotItem.builder()
                                .item(item)
                                .build()
                )
        );
    }

}
