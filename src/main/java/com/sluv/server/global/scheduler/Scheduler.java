package com.sluv.server.global.scheduler;

import com.querydsl.core.Tuple;
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
import com.sluv.server.domain.question.entity.DailyHotQuestion;
import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.question.repository.DailyHotQuestionRepository;
import com.sluv.server.domain.question.repository.QuestionRepository;
import com.sluv.server.domain.search.entity.SearchData;
import com.sluv.server.domain.search.entity.SearchRank;
import com.sluv.server.domain.search.repository.SearchDataRepository;
import com.sluv.server.domain.search.repository.SearchRankRepository;
import com.sluv.server.domain.visit.entity.VisitHistory;
import com.sluv.server.domain.visit.repository.VisitHistoryRepository;
import com.sluv.server.global.cache.CacheService;
import java.time.LocalDateTime;
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
public class Scheduler {
    private final SearchDataRepository searchDataRepository;
    private final SearchRankRepository searchRankRepository;
    private final LuxuryItemRepository luxuryItemRepository;
    private final EfficientItemRepository efficientItemRepository;
    private final WeekHotItemRepository weekHotItemRepository;
    private final DayHotItemRepository dayHotItemRepository;
    private final DailyHotQuestionRepository dailyHotQuestionRepository;
    private final ItemRepository itemRepository;
    private final QuestionRepository questionRepository;
    private final VisitHistoryRepository visitHistoryRepository;
    private final CacheService cacheService;

    /**
     * SearchRank 업데이트
     */
    @Scheduled(cron = "0 0 0 * * *") // 초 분 시 일 월 요일
    public void updateSearchRank() {
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

    /**
     * 일간 HOT Question
     */
    @Scheduled(cron = "0 0 0 * * *") // 초 분 시 일 월 요일
    public void updateDailyHotQuestion() {
        log.info("DailyHotQuestion Update Time: {}", Calendar.getInstance().getTime());

        log.info("Delete Old DailyHotQuestion Data");
        dailyHotQuestionRepository.deleteAll();

        log.info("Get DailyHotQuestion. Time: {}", Calendar.getInstance().getTime());
        List<Question> newDailyHotQuestion = questionRepository.updateDailyHotQuestion();

        log.info("Save DailyHotQuestion. Time: {}", Calendar.getInstance().getTime());

        newDailyHotQuestion.forEach(question ->
                dailyHotQuestionRepository.save(
                        DailyHotQuestion.toEntity(question)
                )
        );
    }

    /**
     * 일일 접속 횟수 저장.
     */
    @Scheduled(cron = "0 0 0 * * *") // 초 분 시 일 월 요일
    public void updateDailyVisitantCount() {
        LocalDateTime now = LocalDateTime.now();
        log.info("VisitantCount Update Time: {}", LocalDateTime.now());
        Long visitantCount = cacheService.getCount();

        log.info("Save RecentDailyVisit. Time: {}", LocalDateTime.now());

        visitHistoryRepository.save(VisitHistory.of(now.minusDays(1), visitantCount));

        cacheService.clear();
    }

}
