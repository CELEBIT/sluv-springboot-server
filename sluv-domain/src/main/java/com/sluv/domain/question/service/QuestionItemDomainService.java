package com.sluv.domain.question.service;

import com.sluv.domain.question.entity.QuestionItem;
import com.sluv.domain.question.repository.QuestionItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionItemDomainService {

    private final QuestionItemRepository questionItemRepository;

    @Transactional(readOnly = true)
    public List<QuestionItem> findAllByQuestionId(Long questionId) {
        return questionItemRepository.findAllByQuestionId(questionId);
    }

    @Transactional
    public void deleteAllByQuestionId(Long questionId) {
        questionItemRepository.deleteAllByQuestionId(questionId);
    }

    @Transactional
    public void saveAll(List<QuestionItem> items) {
        questionItemRepository.saveAll(items);
    }

    @Transactional(readOnly = true)
    public QuestionItem findByQuestionIdAndRepresentFlag(Long questionId, boolean flag) {
        return questionItemRepository.findByQuestionIdAndRepresentFlag(questionId, flag);
    }
}
