package com.sluv.domain.question.repository.impl;

import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.question.entity.*;
import com.sluv.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionRepositoryCustom {
    Page<QuestionBuy> getSearchQuestionBuy(List<Long> questionIdList, Pageable pageable);

    Page<QuestionFind> getSearchQuestionFind(List<Long> questionIdList, Pageable pageable);

    Page<QuestionHowabout> getSearchQuestionHowabout(List<Long> questionIdList, Pageable pageable);

    Page<QuestionRecommend> getSearchQuestionRecommend(List<Long> questionIdList, Pageable pageable);

    Page<Question> getSearchQuestion(List<Long> questionIdList, Pageable pageable);

    List<QuestionBuy> getWaitQuestionBuy(User user, Long questionId, List<Celeb> interestedCeleb, List<Long> blockUserIds);

    List<QuestionRecommend> getWaitQuestionRecommend(User user, Long questionId, List<Long> blockUserIds);

    List<QuestionHowabout> getWaitQuestionHowabout(User user, Long questionId, List<Long> blockUserIds);

    List<QuestionFind> getWaitQuestionFind(User user, Long questionId, List<Celeb> interestedCeleb, List<Long> blockUserIds);

    Page<Question> getUserLikeQuestion(User user, List<Long> blockUserIds, Pageable pageable);

    Page<Question> getUserAllQuestion(User user, Pageable pageable);

    Page<Question> getTotalQuestionList(List<Long> blockUserIds, Pageable pageable);

    Page<QuestionBuy> getQuestionBuyList(String voteStatus, List<Long> blockUserIds, Pageable pageable);

    Page<QuestionFind> getQuestionFindList(Long celebId, Boolean isNewCeleb, List<Long> blockUserIds, Pageable pageable);

    Page<QuestionHowabout> getQuestionHowaboutList(List<Long> blockUserIds, Pageable pageable);

    Page<QuestionRecommend> getQuestionRecommendList(String hashtag, List<Long> blockUserIds, Pageable pageable);

    List<Question> updateDailyHotQuestion();

    Page<Question> getWeeklyHotQuestion(List<Long> blockUserIds, Pageable pageable);

    List<Question> getDailyHotQuestion(List<Long> blockUserIds);

    List<Question> getSearchQuestionIds(String word);

    List<QuestionBuy> getEndTimeBetweenNow(int voteEndCheckPeriod);

    void changeAllNewCelebToCeleb(Celeb celeb, Long newCelebId);
}
