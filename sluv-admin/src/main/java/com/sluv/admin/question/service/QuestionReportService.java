package com.sluv.admin.question.service;

import com.sluv.admin.common.response.PaginationResponse;
import com.sluv.admin.common.service.ReportProcessingService;
import com.sluv.admin.question.dto.QuestionReportDetailDto;
import com.sluv.admin.question.dto.QuestionReportInfoDto;
import com.sluv.admin.question.dto.UpdateQuestionReportResDto;
import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.question.entity.QuestionReport;
import com.sluv.domain.question.enums.QuestionStatus;
import com.sluv.domain.question.service.QuestionReportDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.exception.InvalidReportStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionReportService {

    private final QuestionReportDomainService questionReportDomainService;
    private final ReportProcessingService reportProcessingService;

    @Transactional(readOnly = true)
    public PaginationResponse<QuestionReportInfoDto> getAllQuestionReport(Pageable pageable, ReportStatus reportStatus) {
        Page<QuestionReport> questionReportPage = questionReportDomainService.getAllQuestionReport(pageable, reportStatus);
        List<QuestionReportInfoDto> content = questionReportPage.getContent().stream()
                .map(QuestionReportInfoDto::from)
                .toList();
        return PaginationResponse.create(questionReportPage, content);
    }

    @Transactional(readOnly = true)
    public QuestionReportDetailDto getQuestionReportDetail(Long questionReportId) {
        QuestionReport questionReport = questionReportDomainService.getQuestionReportDetail(questionReportId);
        return QuestionReportDetailDto.from(questionReport);
    }

    public UpdateQuestionReportResDto updateQuestionReportStatus(Long questionReportId, ReportStatus reportStatus) {
        if (reportStatus == ReportStatus.WAITING) {
            throw new InvalidReportStatusException();
        }

        QuestionReport questionReport = questionReportDomainService.findById(questionReportId);


        if (questionReport.getReportStatus() != ReportStatus.WAITING) {
            throw new InvalidReportStatusException();
        }

        User reportedUser = questionReport.getQuestion().getUser();
        User reporterUser = questionReport.getReporter();

        questionReport.changeQuestionReportStatus(reportStatus);

        if (reportStatus == ReportStatus.COMPLETE) {
            questionReport.getQuestion().changeQuestionStatus(QuestionStatus.BLOCKED);
        }
        reportProcessingService.processReport(reportedUser, reporterUser, questionReport.getContent(), reportStatus);

        return UpdateQuestionReportResDto.of(questionReport.getReportStatus());
    }
}
