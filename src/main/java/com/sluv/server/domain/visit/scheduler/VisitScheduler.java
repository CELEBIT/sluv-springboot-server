package com.sluv.server.domain.visit.scheduler;

import com.sluv.server.domain.visit.entity.VisitHistory;
import com.sluv.server.domain.visit.repository.VisitHistoryRepository;
import com.sluv.server.global.cache.CacheService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@Transactional
@RequiredArgsConstructor
public class VisitScheduler {
    private final VisitHistoryRepository visitHistoryRepository;
    private final CacheService cacheService;

    /**
     * 일일 접속 횟수 저장.
     */
    @Scheduled(cron = "0 0 0 * * *") // 초 분 시 일 월 요일
    public void updateDailyVisitantCount() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Daily Visitant Count Update Time: {}", LocalDateTime.now());
        Long visitantCount = cacheService.getVisitantCount();

        log.info("Save RecentDailyVisit. Time: {}", LocalDateTime.now());

        visitHistoryRepository.save(VisitHistory.of(now.minusDays(1), visitantCount));

        cacheService.clearVisitantCount();
    }

}
