package com.sluv.domain.visit.service;

import com.sluv.domain.visit.entity.VisitHistory;
import com.sluv.domain.visit.repository.VisitHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitHistoryDomainService {

    private final VisitHistoryRepository visitHistoryRepository;

    public void saveVisitHistory(LocalDateTime localDateTime, Long visitantCount) {
        VisitHistory visitHistory = VisitHistory.of(localDateTime, visitantCount);
        visitHistoryRepository.save(visitHistory);
    }

    public List<VisitHistory> getVisitHistoryFor10Days() {
        return visitHistoryRepository.getVisitHistoryFor10Days();
    }

}
