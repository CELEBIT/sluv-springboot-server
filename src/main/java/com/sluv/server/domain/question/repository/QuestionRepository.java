package com.sluv.server.domain.question.repository;

import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.question.repository.impl.QuestionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionRepositoryCustom {
}
