package com.sluv.domain.question.service;

import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.QuestionVote;
import com.sluv.domain.question.repository.QuestionVoteRepository;
import com.sluv.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionVoteDomainService {

    private final QuestionVoteRepository questionVoteRepository;

    @Transactional(readOnly = true)
    public QuestionVote findByQuestionIdAndUserIdOrNull(Long questionId, Long userId) {
        return questionVoteRepository.findByQuestionIdAndUserId(questionId, userId).orElse(null);
    }

    @Transactional(readOnly = true)
    public Long countByQuestionId(Long questionId) {
        return questionVoteRepository.countByQuestionId(questionId);
    }

    @Transactional(readOnly = true)
    public List<QuestionVote> findAllByQuestionId(Long questionId) {
        return questionVoteRepository.findAllByQuestionId(questionId);
    }

    @Transactional
    public void deleteById(Long questionVoteId) {
        questionVoteRepository.deleteById(questionVoteId);
    }

    @Transactional
    public QuestionVote saveQuestionVote(Question question, User user, Long voteSortOrder) {
        QuestionVote questionVote = QuestionVote.toEntity(question, user, voteSortOrder);
        return questionVoteRepository.save(questionVote);
    }

}
