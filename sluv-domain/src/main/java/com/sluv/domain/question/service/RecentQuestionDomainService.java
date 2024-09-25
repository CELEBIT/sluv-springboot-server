package com.sluv.domain.question.service;

import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.RecentQuestion;
import com.sluv.domain.question.repository.RecentQuestionRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecentQuestionDomainService {

    private final RecentQuestionRepository recentQuestionRepository;

    @Transactional(readOnly = true)
    public Page<Question> getUserAllRecentQuestion(User user, Pageable pageable) {
        return recentQuestionRepository.getUserAllRecentQuestion(user, pageable);
    }

    @Transactional
    public void deleteAllByUserId(Long userId) {
        recentQuestionRepository.deleteAllByUserId(userId);
    }

    @Transactional
    public RecentQuestion saveRecentQuestion(User nowUser, String qType, Question question) {
        RecentQuestion recentQuestion = RecentQuestion.toEntity(nowUser, qType, question);
        return recentQuestionRepository.save(recentQuestion);
    }

}
