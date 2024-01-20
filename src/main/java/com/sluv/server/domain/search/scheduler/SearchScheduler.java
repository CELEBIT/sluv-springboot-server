package com.sluv.server.domain.search.scheduler;

import com.querydsl.core.Tuple;
import com.sluv.server.domain.search.entity.SearchData;
import com.sluv.server.domain.search.entity.SearchRank;
import com.sluv.server.domain.search.repository.SearchDataRepository;
import com.sluv.server.domain.search.repository.SearchRankRepository;
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
public class SearchScheduler {

    private final SearchDataRepository searchDataRepository;
    private final SearchRankRepository searchRankRepository;

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
}
