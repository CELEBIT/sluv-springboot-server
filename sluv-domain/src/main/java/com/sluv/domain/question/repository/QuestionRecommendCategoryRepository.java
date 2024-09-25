package com.sluv.domain.question.repository;

import com.sluv.domain.question.entity.QuestionRecommendCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRecommendCategoryRepository extends JpaRepository<QuestionRecommendCategory, Long> {
    void deleteAllByQuestionId(Long id);

    QuestionRecommendCategory findOneByQuestionId(Long id);

    List<QuestionRecommendCategory> findAllByQuestionId(Long questionId);
}
