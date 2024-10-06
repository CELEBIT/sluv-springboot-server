package com.sluv.admin.visit.service;

import com.sluv.admin.visit.dto.VisitHistoryCountResDto;
import com.sluv.domain.visit.entity.DailyVisit;
import com.sluv.domain.visit.entity.VisitHistory;
import com.sluv.domain.visit.service.DailyVisitDomainService;
import com.sluv.domain.visit.service.VisitHistoryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitHistoryService {

    private final VisitHistoryDomainService visitHistoryDomainService;
    private final DailyVisitDomainService dailyVisitDomainService;

    public VisitHistoryCountResDto getVisitHistoryCount() {
        List<VisitHistory> visitHistoryFor10Days = visitHistoryDomainService.getVisitHistoryFor10Days();
        List<DailyVisit> allDailyVisit = dailyVisitDomainService.findAll();
        LocalDateTime now = LocalDateTime.now();
        Long todayCount = getDayCount(now, allDailyVisit);
        Long yesterdayCount = getDayCount(now.minusDays(1), allDailyVisit);
        List<Long> countGraph = getCountGraph(visitHistoryFor10Days, todayCount);

        return VisitHistoryCountResDto.of(todayCount, yesterdayCount, countGraph);
    }

    private Long getDayCount(LocalDateTime now, List<DailyVisit> allDailyVisit) {
        return allDailyVisit.stream()
                .filter(dailyVisit -> dailyVisit.getCreatedAt().getDayOfMonth() == now.getDayOfMonth())
                .count();
    }

    private static List<Long> getCountGraph(List<VisitHistory> visitHistoryFor10Days, Long todayCount) {
        List<Long> countGraph = new ArrayList<>();
        for (VisitHistory visitHistory : visitHistoryFor10Days) {
            countGraph.add(visitHistory.getVisitCount());
        }
        countGraph.add(todayCount);
        return countGraph;
    }
}
