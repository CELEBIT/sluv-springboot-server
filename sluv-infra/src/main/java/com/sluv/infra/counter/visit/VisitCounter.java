package com.sluv.infra.counter.visit;

import org.springframework.stereotype.Component;

@Component
public interface VisitCounter {
    void countVisit(Long memberId);

    Long getVisitantCount();

    void clearVisitantCount();

}
