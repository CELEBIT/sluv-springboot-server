package com.sluv.domain.question.service;

import com.sluv.domain.question.entity.QuestionImg;
import com.sluv.domain.question.repository.QuestionImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionImgDomainService {

    private final QuestionImgRepository questionImgRepository;

    public List<QuestionImg> findAllByQuestionId(Long questionId) {
        return questionImgRepository.findAllByQuestionId(questionId);
    }

    public void deleteAllByQuestionId(Long questionId) {
        questionImgRepository.deleteAllByQuestionId(questionId);
    }

    public void saveAll(List<QuestionImg> images) {
        questionImgRepository.saveAll(images);
    }

    public QuestionImg findByQuestionIdAndRepresentFlag(Long questionId, boolean flag) {
        return questionImgRepository.findByQuestionIdAndRepresentFlag(questionId, flag);
    }
}
