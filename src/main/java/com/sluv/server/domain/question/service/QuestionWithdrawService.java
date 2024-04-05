package com.sluv.server.domain.question.service;

import com.sluv.server.domain.question.repository.QuestionLikeRepository;
import com.sluv.server.domain.question.repository.QuestionReportRepository;
import com.sluv.server.domain.question.repository.QuestionRepository;
import com.sluv.server.domain.question.repository.QuestionVoteRepository;
import com.sluv.server.domain.question.repository.RecentQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionWithdrawService {
    private final QuestionRepository questionRepository;
    private final QuestionLikeRepository questionLikeRepository;
    private final QuestionReportRepository questionReportRepository;
    private final QuestionVoteRepository questionVoteRepository;
    private final RecentQuestionRepository recentQuestionRepository;

    public void withdrawQuestionByUserId(Long userId) {
        questionRepository.withdrawByUserId(userId);
        questionLikeRepository.withdrawByUserId(userId);
        questionReportRepository.withdrawByUserId(userId);
        questionVoteRepository.withdrawByUserId(userId);
        recentQuestionRepository.deleteAllByUserId(userId);
    }
}
