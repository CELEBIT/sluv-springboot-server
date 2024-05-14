package com.sluv.server.domain.question.repository.impl;

import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.question.entity.QuestionBuy;
import com.sluv.server.domain.question.entity.QuestionFind;
import com.sluv.server.domain.question.entity.QuestionHowabout;
import com.sluv.server.domain.question.entity.QuestionRecommend;
import com.sluv.server.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionRepositoryCustom {
    Page<QuestionBuy> getSearchQuestionBuy(List<Long> questionIdList, Pageable pageable);

    Page<QuestionFind> getSearchQuestionFind(List<Long> questionIdList, Pageable pageable);

    Page<QuestionHowabout> getSearchQuestionHowabout(List<Long> questionIdList, Pageable pageable);

    Page<QuestionRecommend> getSearchQuestionRecommend(List<Long> questionIdList, Pageable pageable);

    Page<Question> getSearchQuestion(List<Long> questionIdList, Pageable pageable);

    List<QuestionBuy> getWaitQuestionBuy(User user, Long questionId, List<Celeb> interestedCeleb);

    List<QuestionRecommend> getWaitQuestionRecommend(User user, Long questionId);

    List<QuestionHowabout> getWaitQuestionHowabout(User user, Long questionId);

    List<QuestionFind> getWaitQuestionFind(User user, Long questionId, List<Celeb> interestedCeleb);

    Page<Question> getUserLikeQuestion(User user, Pageable pageable);

    Page<Question> getUserAllQuestion(User user, Pageable pageable);

    Page<Question> getTotalQuestionList(Pageable pageable);

    Page<QuestionBuy> getQuestionBuyList(String voteStatus, Pageable pageable);

    Page<QuestionFind> getQuestionFindList(Long celebId, Pageable pageable);

    Page<QuestionHowabout> getQuestionHowaboutList(Pageable pageable);

    Page<QuestionRecommend> getQuestionRecommendList(String hashtag, Pageable pageable);

    List<Question> updateDailyHotQuestion();

    Page<Question> getWeeklyHotQuestion(Pageable pageable);

    List<Question> getDailyHotQuestion();

    List<Question> getSearchQuestionIds(String word);

}
