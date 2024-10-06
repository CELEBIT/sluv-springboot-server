package com.sluv.domain.question.service;

import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.QuestionReport;
import com.sluv.domain.question.enums.QuestionReportReason;
import com.sluv.domain.question.exception.QuestionReportNotFoundException;
import com.sluv.domain.question.repository.QuestionReportRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionReportDomainService {

    private final QuestionReportRepository questionReportRepository;

    public Boolean existsByQuestionIdAndReporterId(Long questionId, Long reporterId) {
        return questionReportRepository.existsByQuestionIdAndReporterId(questionId, reporterId);
    }

    public QuestionReport saveQuestionReport(User user, Question question, QuestionReportReason reason,
                                             String content) {
        QuestionReport questionReport = QuestionReport.toEntity(user, question, reason, content);
        return questionReportRepository.save(questionReport);
    }

    public Page<QuestionReport> getAllQuestionReport(Pageable pageable, ReportStatus reportStatus) {
        return questionReportRepository.getAllQuestionReport(pageable, reportStatus);
    }

    public QuestionReport getQuestionReportDetail(Long questionReportId) {
        return questionReportRepository.getQuestionReportDetail(questionReportId)
                .orElseThrow(QuestionReportNotFoundException::new);
    }

    public QuestionReport findById(Long questionReportId) {
        return questionReportRepository.findById(questionReportId).orElseThrow(QuestionReportNotFoundException::new);
    }
}
