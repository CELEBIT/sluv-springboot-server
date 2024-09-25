package com.sluv.domain.question.repository;

import com.sluv.domain.question.entity.DailyHotQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyHotQuestionRepository extends JpaRepository<DailyHotQuestion, Long> {
}
