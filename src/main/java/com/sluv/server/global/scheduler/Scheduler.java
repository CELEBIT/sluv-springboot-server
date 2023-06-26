package com.sluv.server.global.scheduler;

import com.querydsl.core.Tuple;
import com.sluv.server.domain.item.entity.EfficientItem;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.LuxuryItem;
import com.sluv.server.domain.item.repository.EfficientItemRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.item.repository.LuxuryItemRepository;
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

        List<SearchRank> result = topData.stream().map(data -> SearchRank.builder()
                .searchWord(data.get(0, SearchData.class).getSearchWord())
                .searchCount(data.get(1, Long.class))
                .build()
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

}
