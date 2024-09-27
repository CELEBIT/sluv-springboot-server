package com.sluv.batch.search;

import com.sluv.domain.search.entity.SearchRank;
import com.sluv.domain.search.service.SearchDataDomainService;
import com.sluv.domain.search.service.SearchRankDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SearchScheduler {

    private final SearchDataDomainService searchDataDomainService;
    private final SearchRankDomainService searchRankDomainService;

    /**
     * SearchRank 업데이트
     */
    @Transactional
    @Scheduled(cron = "0 0 17 * * *") // 초 분 시 일 월 요일
    public void updateSearchRank() {
        log.info("SearchRank Update Time: {}", Calendar.getInstance().getTime());

        log.info("Delete Old SearchRank Data");
        searchRankDomainService.deleteAllSearchRank();

        log.info("Get SearchData. Time: {}", Calendar.getInstance().getTime());
        List<SearchRank> result = searchDataDomainService.getTopData();

        log.info("Save SearchRank. Time: {}", Calendar.getInstance().getTime());
        searchRankDomainService.saveAllSearchRank(result);
    }
}
