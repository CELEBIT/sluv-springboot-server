package com.sluv.domain.visit.repository;

import com.sluv.domain.visit.entity.VisitHistory;
import com.sluv.domain.visit.repository.impl.VisitHistoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitHistoryRepository extends JpaRepository<VisitHistory, Long>, VisitHistoryRepositoryCustom {
}
