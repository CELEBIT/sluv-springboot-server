package com.sluv.domain.question.service;

import com.sluv.domain.question.entity.QuestionItem;
import com.sluv.domain.question.repository.QuestionItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionItemDomainService {

    private final QuestionItemRepository questionItemRepository;

    public List<QuestionItem> findAllByQuestionId(Long questionId) {
        return questionItemRepository.findAllByQuestionId(questionId);
    }

    public void deleteAllByQuestionId(Long questionId) {
        questionItemRepository.deleteAllByQuestionId(questionId);
    }

    public void saveAll(List<QuestionItem> items) {
        questionItemRepository.saveAll(items);
    }

    public QuestionItem findByQuestionIdAndRepresentFlag(Long questionId, boolean flag) {
        return questionItemRepository.findByQuestionIdAndRepresentFlag(questionId, flag);
    }
}
