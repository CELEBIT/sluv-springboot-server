package com.sluv.domain.question.service;

import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.QuestionLike;
import com.sluv.domain.question.repository.QuestionLikeRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionLikeDomainService {

    private final QuestionLikeRepository questionLikeRepository;

    @Transactional(readOnly = true)
    public Long countByQuestionId(Long questionId) {
        return questionLikeRepository.countByQuestionId(questionId);
    }

    @Transactional(readOnly = true)
    public Boolean existsByQuestionIdAndUserId(Long questionId, Long userId) {
        return questionLikeRepository.existsByQuestionIdAndUserId(questionId, userId);
    }

    @Transactional
    public void deleteByQuestionIdAndUserId(Long questionId, Long userId) {
        questionLikeRepository.deleteByQuestionIdAndUserId(questionId, userId);
    }

    @Transactional
    public QuestionLike saveQuestionLike(User user, Question question) {
        QuestionLike questionLike = QuestionLike.toEntity(user, question);
        return questionLikeRepository.save(questionLike);
    }

}
