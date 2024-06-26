package com.sluv.server.domain.question.repository.impl;

import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecentQuestionRepositoryCustom {
    Page<Question> getUserAllRecentQuestion(User user, Pageable pageable);
}
