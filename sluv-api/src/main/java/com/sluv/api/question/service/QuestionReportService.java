package com.sluv.api.question.service;

import com.sluv.api.question.dto.QuestionReportReqDto;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.exception.QuestionReportDuplicateException;
import com.sluv.domain.question.service.QuestionDomainService;
import com.sluv.domain.question.service.QuestionReportDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionReportService {

    private final UserDomainService userDomainService;
    private final QuestionDomainService questionDomainService;
    private final QuestionReportDomainService questionReportDomainService;

    @Transactional
    public void postQuestionReport(Long userId, Long questionId, QuestionReportReqDto dto) {
        User user = userDomainService.findById(userId);
        log.info("질문 게시글 신고 - 사용자 : {}, 질문 게시글 : {}, 사유 : {}", user.getId(), questionId, dto.getReason());

        Boolean reportExists = questionReportDomainService.existsByQuestionIdAndReporterId(questionId, user.getId());
        if (reportExists) {
            throw new QuestionReportDuplicateException();
        }

        Question question = questionDomainService.findById(questionId);
        questionReportDomainService.saveQuestionReport(user, question, dto.getReason(), dto.getContent());
    }
}
