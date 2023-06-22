package com.sluv.server.domain.question.repository.impl;

import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.question.entity.*;
import com.sluv.server.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionRepositoryCustom {
    Page<QuestionBuy> getSearchQuestionBuy(List<Long> questionIdList, Pageable pageable);

    Page<QuestionFind> getSearchQuestionFind(List<Long> questionIdList, Pageable pageable);

    Page<QuestionHowabout> getSearchQuestionHowabout(List<Long> questionIdList, Pageable pageable);

    Page<QuestionRecommend> getSearchQuestionRecommend(List<Long> questionIdList, Pageable pageable);

    List<QuestionBuy> getWaitQuestionBuy(User user, Long questionId, List<Celeb> interestedCeleb);
}
