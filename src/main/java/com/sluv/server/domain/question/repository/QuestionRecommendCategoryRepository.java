package com.sluv.server.domain.question.repository;

import com.sluv.server.domain.question.entity.QuestionRecommendCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRecommendCategoryRepository extends JpaRepository<QuestionRecommendCategory, Long> {
}
