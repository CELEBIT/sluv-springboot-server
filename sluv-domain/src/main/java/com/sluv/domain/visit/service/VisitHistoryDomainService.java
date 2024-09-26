package com.sluv.domain.visit.service;

import com.sluv.domain.visit.entity.VisitHistory;
import com.sluv.domain.visit.repository.VisitHistoryRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VisitHistoryDomainService {

    private final VisitHistoryRepository visitHistoryRepository;

    @Transactional
    public void saveVisitHistory(LocalDateTime localDateTime, Long visitantCount) {
        VisitHistory visitHistory = VisitHistory.of(localDateTime, visitantCount);
        visitHistoryRepository.save(visitHistory);
    }

}
