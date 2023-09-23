package com.sluv.server.domain.question.repository;

import com.sluv.server.domain.question.entity.DailyHotQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyHotQuestionRepository extends JpaRepository<DailyHotQuestion, Long> {
}
