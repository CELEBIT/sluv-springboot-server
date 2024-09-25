package com.sluv.domain.question.service;

import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.QuestionReport;
import com.sluv.domain.question.enums.QuestionReportReason;
import com.sluv.domain.question.repository.QuestionReportRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionReportDomainService {

    private final QuestionReportRepository questionReportRepository;

    @Transactional(readOnly = true)
    public Boolean existsByQuestionIdAndReporterId(Long questionId, Long reporterId) {
        return questionReportRepository.existsByQuestionIdAndReporterId(questionId, reporterId);
    }

    @Transactional
    public QuestionReport saveQuestionReport(User user, Question question, QuestionReportReason reason,
                                             String content) {
        QuestionReport questionReport = QuestionReport.toEntity(user, question, reason, content);
        return questionReportRepository.save(questionReport);
    }
}
