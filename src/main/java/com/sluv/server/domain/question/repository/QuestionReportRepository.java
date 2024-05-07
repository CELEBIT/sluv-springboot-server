package com.sluv.server.domain.question.repository;

import com.sluv.server.domain.question.entity.QuestionReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionReportRepository extends JpaRepository<QuestionReport, Long> {
    Boolean existsByQuestionIdAndReporterId(Long questionId, Long reporterId);
}
