package com.sluv.server.domain.question.repository;

import com.sluv.server.domain.question.entity.QuestionRecommendCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRecommendCategoryRepository extends JpaRepository<QuestionRecommendCategory, Long> {
    void deleteAllByQuestionId(Long id);

    QuestionRecommendCategory findOneByQuestionId(Long id);

    List<QuestionRecommendCategory> findAllByQuestionId(Long questionId);
}
