package com.sluv.domain.question.repository;

import com.sluv.domain.question.entity.RecentQuestion;
import com.sluv.domain.question.repository.impl.RecentQuestionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentQuestionRepository extends JpaRepository<RecentQuestion, Long>, RecentQuestionRepositoryCustom {

    void deleteAllByUserId(Long userId);
}
