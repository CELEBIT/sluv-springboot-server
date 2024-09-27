package com.sluv.domain.question.repository;

import com.sluv.domain.question.entity.QuestionItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionItemRepository extends JpaRepository<QuestionItem, Long> {
    void deleteAllByQuestionId(Long id);

    List<QuestionItem> findAllByQuestionId(Long questionId);

    QuestionItem findByQuestionIdAndRepresentFlag(Long questionId, boolean b);

    QuestionItem findOneByQuestionId(Long questionId);
}
