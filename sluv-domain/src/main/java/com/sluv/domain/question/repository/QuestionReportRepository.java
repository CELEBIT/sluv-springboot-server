package com.sluv.domain.question.repository;

import com.sluv.domain.question.entity.QuestionReport;
import com.sluv.domain.question.repository.impl.QuestionReportRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionReportRepository extends JpaRepository<QuestionReport, Long>, QuestionReportRepositoryCustom {
    Boolean existsByQuestionIdAndReporterId(Long questionId, Long reporterId);
}
