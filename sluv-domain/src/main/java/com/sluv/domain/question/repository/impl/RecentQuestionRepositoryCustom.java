package com.sluv.domain.question.repository.impl;

import com.sluv.domain.question.entity.Question;
import com.sluv.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecentQuestionRepositoryCustom {
    Page<Question> getUserAllRecentQuestion(User user, List<Long> blockUserIds, Pageable pageable);
}
