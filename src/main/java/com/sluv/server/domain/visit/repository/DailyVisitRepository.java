package com.sluv.server.domain.visit.repository;

import com.sluv.server.domain.visit.entity.DailyVisit;
import com.sluv.server.domain.visit.repository.impl.DailyVisitRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyVisitRepository extends JpaRepository<DailyVisit, Long>, DailyVisitRepositoryCustom {
}
