package com.sluv.batch.visit;

import com.sluv.domain.visit.service.VisitHistoryDomainService;
import com.sluv.infra.counter.visit.VisitCounter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class VisitScheduler {
    private final VisitHistoryDomainService visitHistoryDomainService;
    private final VisitCounter visitCounter;

    /**
     * 일일 접속 횟수 저장.
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 초 분 시 일 월 요일
    public void updateDailyVisitantCount() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Daily Visitant Count Update Time: {}", LocalDateTime.now());
        Long visitantCount = visitCounter.getVisitantCount();

        log.info("Save RecentDailyVisit. Time: {}", LocalDateTime.now());

        visitHistoryDomainService.saveVisitHistory(now.minusDays(1), visitantCount);

        visitCounter.clearVisitantCount();
    }

}
