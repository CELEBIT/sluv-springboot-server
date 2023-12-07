package com.sluv.server.domain.visit.repository;

import com.sluv.server.domain.user.entity.User;
import java.time.LocalDateTime;

public interface DailyVisitRepositoryCustom {
    boolean existUserToday(LocalDateTime now, User user);
}
