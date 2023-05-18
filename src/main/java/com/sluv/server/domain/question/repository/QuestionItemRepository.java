package com.sluv.server.domain.question.repository;

import com.sluv.server.domain.question.entity.QuestionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionItemRepository extends JpaRepository<QuestionItem, Long> {
    void deleteAllByQuestionId(Long id);
}
