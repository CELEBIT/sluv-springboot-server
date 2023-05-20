package com.sluv.server.domain.question.repository;

import com.sluv.server.domain.question.entity.QuestionLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionLikeRepository extends JpaRepository<QuestionLike, Long> {
    Boolean existsByQuestionIdAndUserId(Long questionId, Long userId);

    void deleteByQuestionIdAndUserId(Long questionId, Long id);

    Long countByQuestionId(Long questionId);
}
