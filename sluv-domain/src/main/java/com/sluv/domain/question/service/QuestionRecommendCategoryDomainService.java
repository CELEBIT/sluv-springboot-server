package com.sluv.domain.question.service;

import com.sluv.domain.question.entity.QuestionRecommendCategory;
import com.sluv.domain.question.repository.QuestionRecommendCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionRecommendCategoryDomainService {

    private final QuestionRecommendCategoryRepository questionRecommendCategoryRepository;

    public List<QuestionRecommendCategory> findAllByQuestionId(Long questionId) {
        return questionRecommendCategoryRepository.findAllByQuestionId(questionId);
    }

    public void deleteAllByQuestionId(Long questionId) {
        questionRecommendCategoryRepository.deleteAllByQuestionId(questionId);
    }

    public void saveAll(List<QuestionRecommendCategory> recommendCategories) {
        questionRecommendCategoryRepository.saveAll(recommendCategories);
    }
}
