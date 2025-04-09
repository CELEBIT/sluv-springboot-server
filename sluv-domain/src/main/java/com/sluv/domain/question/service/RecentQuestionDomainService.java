package com.sluv.domain.question.service;

import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.RecentQuestion;
import com.sluv.domain.question.repository.RecentQuestionRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentQuestionDomainService {

    private final RecentQuestionRepository recentQuestionRepository;

    public Page<Question> getUserAllRecentQuestion(User user, List<Long> blockUserIds, Pageable pageable) {
        return recentQuestionRepository.getUserAllRecentQuestion(user, blockUserIds, pageable);
    }

    public void deleteAllByUserId(Long userId) {
        recentQuestionRepository.deleteAllByUserId(userId);
    }

    public RecentQuestion saveRecentQuestion(User nowUser, String qType, Question question) {
        RecentQuestion recentQuestion = RecentQuestion.toEntity(nowUser, qType, question);
        return recentQuestionRepository.save(recentQuestion);
    }

}
