package com.sluv.domain.question.repository;

import com.sluv.domain.question.entity.QuestionVote;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionVoteRepository extends JpaRepository<QuestionVote, Long> {
    Long countByQuestionIdAndVoteSortOrder(Long question_id, Long voteSortOrder);

    Long countByQuestionId(Long questionId);

    Optional<QuestionVote> findByQuestionIdAndUserId(Long questionId, Long userId);

    Boolean existsByQuestionIdAndUserId(Long questionId, Long id);

    List<QuestionVote> findAllByQuestionId(Long questionId);
}
