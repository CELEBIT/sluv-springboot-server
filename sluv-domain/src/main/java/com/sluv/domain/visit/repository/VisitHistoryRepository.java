package com.sluv.domain.visit.repository;

import com.sluv.domain.visit.entity.VisitHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitHistoryRepository extends JpaRepository<VisitHistory, Long> {
}
