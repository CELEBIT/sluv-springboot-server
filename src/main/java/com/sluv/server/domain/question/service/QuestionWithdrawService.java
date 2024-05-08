package com.sluv.server.domain.question.service;

import com.sluv.server.domain.question.repository.RecentQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionWithdrawService {
    private final RecentQuestionRepository recentQuestionRepository;

    public void withdrawQuestionByUserId(Long userId) {
        recentQuestionRepository.deleteAllByUserId(userId);
    }
}
