package com.sluv.server.domain.question.repository;

import com.sluv.server.domain.question.entity.QuestionVote;
import com.sluv.server.domain.question.repository.impl.QuestionVoteRepositoryCustom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionVoteRepository extends JpaRepository<QuestionVote, Long>, QuestionVoteRepositoryCustom {
    Long countByQuestionIdAndVoteSortOrder(Long question_id, Long voteSortOrder);

    Long countByQuestionId(Long questionId);

    Optional<QuestionVote> findByQuestionIdAndUserId(Long questionId, Long userId);

    Boolean existsByQuestionIdAndUserId(Long questionId, Long id);

    List<QuestionVote> findAllByQuestionId(Long questionId);
}
