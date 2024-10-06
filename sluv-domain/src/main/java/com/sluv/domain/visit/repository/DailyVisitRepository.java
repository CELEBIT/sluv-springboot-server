package com.sluv.domain.visit.repository;

import com.sluv.domain.visit.entity.DailyVisit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyVisitRepository extends JpaRepository<DailyVisit, Long> {
}
