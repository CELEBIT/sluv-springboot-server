package com.sluv.server.domain.question.repository;

import com.sluv.server.domain.question.entity.QuestionImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionImgRepository extends JpaRepository<QuestionImg, Long> {
    void deleteAllByQuestionId(Long id);

    List<QuestionImg> findAllByQuestionId(Long questionId);

    QuestionImg findByQuestionIdAndRepresentFlag(Long questionId, boolean b);

    QuestionImg findOneByQuestionId(Long questionId);
}
