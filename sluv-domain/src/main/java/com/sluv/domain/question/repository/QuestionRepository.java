package com.sluv.domain.question.repository;

import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.enums.QuestionStatus;
import com.sluv.domain.question.repository.impl.QuestionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionRepositoryCustom {
    Long countByUserIdAndQuestionStatus(Long user_id, QuestionStatus questionStatus);
}
