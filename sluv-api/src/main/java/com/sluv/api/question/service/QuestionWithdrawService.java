package com.sluv.api.question.service;

import com.sluv.domain.question.service.RecentQuestionDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionWithdrawService {
    private final RecentQuestionDomainService recentQuestionDomainService;

    public void withdrawQuestionByUserId(Long userId) {
        recentQuestionDomainService.deleteAllByUserId(userId);
    }

}
