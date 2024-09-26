package com.sluv.domain.question.service;

import com.sluv.domain.question.entity.QuestionRecommendCategory;
import com.sluv.domain.question.repository.QuestionRecommendCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionRecommendCategoryDomainService {

    private final QuestionRecommendCategoryRepository questionRecommendCategoryRepository;

    @Transactional(readOnly = true)
    public List<QuestionRecommendCategory> findAllByQuestionId(Long questionId) {
        return questionRecommendCategoryRepository.findAllByQuestionId(questionId);
    }

    @Transactional
    public void deleteAllByQuestionId(Long questionId) {
        questionRecommendCategoryRepository.deleteAllByQuestionId(questionId);
    }

    @Transactional
    public void saveAll(List<QuestionRecommendCategory> recommendCategories) {
        questionRecommendCategoryRepository.saveAll(recommendCategories);
    }
}
