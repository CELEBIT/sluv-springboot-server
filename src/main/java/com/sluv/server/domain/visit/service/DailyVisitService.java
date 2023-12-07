package com.sluv.server.domain.visit.service;

import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.visit.entity.DailyVisit;
import com.sluv.server.domain.visit.repository.DailyVisitRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DailyVisitService {
    private final DailyVisitRepository dailyVisitRepository;

    @Async
    public void saveDailyVisit(User user) {
        LocalDateTime now = LocalDateTime.now();
        boolean exist = dailyVisitRepository.existUserToday(now, user);
        if (!exist) {
            dailyVisitRepository.save(DailyVisit.of(user));
        }
    }
}
