package com.sluv.server.domain.question.repository;

import com.sluv.server.domain.question.entity.RecentQuestion;
import com.sluv.server.domain.question.repository.impl.RecentQuestionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentQuestionRepository extends JpaRepository<RecentQuestion, Long>, RecentQuestionRepositoryCustom {

}
