package com.sluv.domain.question.repository.impl;

import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.question.entity.QuestionReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QuestionReportRepositoryCustom {

    Page<QuestionReport> getAllQuestionReport(Pageable pageable, ReportStatus reportStatus);

    Optional<QuestionReport> getQuestionReportDetail(Long questionReportId);
}
