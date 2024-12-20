package com.sluv.domain.question.service;

import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.question.entity.*;
import com.sluv.domain.question.enums.QuestionStatus;
import com.sluv.domain.question.exception.QuestionNotFoundException;
import com.sluv.domain.question.repository.QuestionRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionDomainService {

    private final QuestionRepository questionRepository;

    public Question findById(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);
    }

    public Page<Question> getUserLikeQuestion(User user, Pageable pageable) {
        return questionRepository.getUserLikeQuestion(user, pageable);
    }

    public Long countByUserIdAndQuestionStatus(Long questionId, QuestionStatus questionStatus) {
        return questionRepository.countByUserIdAndQuestionStatus(questionId, questionStatus);
    }

    public Page<Question> getUserAllQuestion(User user, Pageable pageable) {
        return questionRepository.getUserAllQuestion(user, pageable);
    }

    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    public List<QuestionBuy> getWaitQuestionBuy(User user, Long questionId, List<Celeb> interestedCelebs) {
        return questionRepository.getWaitQuestionBuy(user, questionId, interestedCelebs);
    }

    public List<QuestionRecommend> getWaitQuestionRecommend(User user, Long questionId) {
        return questionRepository.getWaitQuestionRecommend(user, questionId);
    }

    public List<QuestionHowabout> getWaitQuestionHowabout(User user, Long questionId) {
        return questionRepository.getWaitQuestionHowabout(user, questionId);
    }

    public List<QuestionFind> getWaitQuestionFind(User user, Long questionId, List<Celeb> interestedCelebs) {
        return questionRepository.getWaitQuestionFind(user, questionId, interestedCelebs);
    }

    public Page<Question> getTotalQuestionList(Pageable pageable) {
        return questionRepository.getTotalQuestionList(pageable);
    }

    public Page<QuestionBuy> getQuestionBuyList(String voteStatus, Pageable pageable) {
        return questionRepository.getQuestionBuyList(voteStatus, pageable);
    }

    public Page<QuestionFind> getQuestionFindList(Long celebId, Pageable pageable) {
        return questionRepository.getQuestionFindList(celebId, pageable);
    }

    public Page<QuestionHowabout> getQuestionHowaboutList(Pageable pageable) {
        return questionRepository.getQuestionHowaboutList(pageable);
    }

    public Page<QuestionRecommend> getQuestionRecommendList(String hashtag, Pageable pageable) {
        return questionRepository.getQuestionRecommendList(hashtag, pageable);
    }

    public List<Question> getDailyHotQuestion() {
        return questionRepository.getDailyHotQuestion();
    }

    public Page<?> getSearchQuestion(List<Long> searchQuestionIds, Pageable pageable) {
        return questionRepository.getSearchQuestion(searchQuestionIds, pageable);
    }

    public Page<?> getSearchQuestionBuy(List<Long> searchQuestionIds, Pageable pageable) {
        return questionRepository.getSearchQuestionBuy(searchQuestionIds, pageable);
    }

    public Page<?> getSearchQuestionFind(List<Long> searchQuestionIds, Pageable pageable) {
        return questionRepository.getSearchQuestionFind(searchQuestionIds, pageable);
    }

    public Page<?> getSearchQuestionHowabout(List<Long> searchQuestionIds, Pageable pageable) {
        return questionRepository.getSearchQuestionHowabout(searchQuestionIds, pageable);
    }

    public Page<?> getSearchQuestionRecommend(List<Long> searchQuestionIds, Pageable pageable) {
        return questionRepository.getSearchQuestionRecommend(searchQuestionIds, pageable);
    }

    public Page<Question> getWeeklyHotQuestion(Pageable pageable) {
        return questionRepository.getWeeklyHotQuestion(pageable);
    }

    public void changeAllNewCelebToCeleb(Celeb celeb, Long newCelebId) {
        questionRepository.changeAllNewCelebToCeleb(celeb, newCelebId);
    }
}
