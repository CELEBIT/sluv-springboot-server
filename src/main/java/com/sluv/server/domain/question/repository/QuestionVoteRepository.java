package com.sluv.server.domain.question.repository;

import com.sluv.server.domain.question.entity.QuestionVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionVoteRepository extends JpaRepository<QuestionVote, Long> {
    Long countByQuestionIdAndVoteSortOrder(Long question_id, Long voteSortOrder);

    Long countByQuestionId(Long questionId);
}
