package com.sluv.domain.question.repository.impl;

import com.sluv.domain.question.entity.Question;
import com.sluv.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecentQuestionRepositoryCustom {
    Page<Question> getUserAllRecentQuestion(User user, Pageable pageable);
}
