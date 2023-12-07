package com.sluv.server.domain.visit.repository.impl;

import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.visit.entity.DailyVisit;
import java.time.LocalDateTime;
import java.util.List;

public interface DailyVisitRepositoryCustom {
    boolean existUserToday(LocalDateTime now, User user);

    List<DailyVisit> getRecentDailyVisit(LocalDateTime now);

    void deleteTwoDaysAgo(LocalDateTime now);
}
