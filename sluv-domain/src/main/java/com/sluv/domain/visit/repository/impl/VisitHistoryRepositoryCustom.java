package com.sluv.domain.visit.repository.impl;


import com.sluv.domain.visit.entity.VisitHistory;

import java.util.List;

public interface VisitHistoryRepositoryCustom {
    List<VisitHistory> getVisitHistoryFor10Days();
}
