package com.sluv.domain.question.service;

import com.sluv.domain.question.entity.QuestionImg;
import com.sluv.domain.question.repository.QuestionImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionImgDomainService {

    private final QuestionImgRepository questionImgRepository;

    @Transactional(readOnly = true)
    public List<QuestionImg> findAllByQuestionId(Long questionId) {
        return questionImgRepository.findAllByQuestionId(questionId);
    }

    @Transactional
    public void deleteAllByQuestionId(Long questionId) {
        questionImgRepository.deleteAllByQuestionId(questionId);
    }

    @Transactional
    public void saveAll(List<QuestionImg> images) {
        questionImgRepository.saveAll(images);
    }

    @Transactional(readOnly = true)
    public QuestionImg findByQuestionIdAndRepresentFlag(Long questionId, boolean flag) {
        return questionImgRepository.findByQuestionIdAndRepresentFlag(questionId, flag);
    }
}
