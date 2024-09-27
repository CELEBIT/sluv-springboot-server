package com.sluv.domain.question.service;

import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.QuestionVote;
import com.sluv.domain.question.repository.QuestionVoteRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionVoteDomainService {

    private final QuestionVoteRepository questionVoteRepository;

    public QuestionVote findByQuestionIdAndUserIdOrNull(Long questionId, Long userId) {
        return questionVoteRepository.findByQuestionIdAndUserId(questionId, userId).orElse(null);
    }

    public Long countByQuestionId(Long questionId) {
        return questionVoteRepository.countByQuestionId(questionId);
    }

    public List<QuestionVote> findAllByQuestionId(Long questionId) {
        return questionVoteRepository.findAllByQuestionId(questionId);
    }

    public void deleteById(Long questionVoteId) {
        questionVoteRepository.deleteById(questionVoteId);
    }

    public QuestionVote saveQuestionVote(Question question, User user, Long voteSortOrder) {
        QuestionVote questionVote = QuestionVote.toEntity(question, user, voteSortOrder);
        return questionVoteRepository.save(questionVote);
    }

}
