package com.sluv.domain.question.repository;

import com.sluv.domain.question.entity.QuestionImg;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionImgRepository extends JpaRepository<QuestionImg, Long> {
    void deleteAllByQuestionId(Long id);

    List<QuestionImg> findAllByQuestionId(Long questionId);

    QuestionImg findByQuestionIdAndRepresentFlag(Long questionId, boolean b);

    QuestionImg findOneByQuestionId(Long questionId);
}
