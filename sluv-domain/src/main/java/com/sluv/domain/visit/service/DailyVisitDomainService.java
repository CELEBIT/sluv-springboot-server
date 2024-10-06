package com.sluv.domain.visit.service;

import com.sluv.domain.visit.entity.DailyVisit;
import com.sluv.domain.visit.repository.DailyVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyVisitDomainService {

    private final DailyVisitRepository dailyVisitRepository;

    public List<DailyVisit> findAll() {
        return dailyVisitRepository.findAll();
    }

}
