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

    public Page<Question> getUserLikeQuestion(User user, List<Long> blockUserIds, Pageable pageable) {
        return questionRepository.getUserLikeQuestion(user, blockUserIds, pageable);
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

    public List<QuestionBuy> getWaitQuestionBuy(User user, Long questionId, List<Celeb> interestedCelebs, List<Long> blockUserIds) {
        return questionRepository.getWaitQuestionBuy(user, questionId, interestedCelebs, blockUserIds);
    }

    public List<QuestionRecommend> getWaitQuestionRecommend(User user, Long questionId, List<Long> blockUserIds) {
        return questionRepository.getWaitQuestionRecommend(user, questionId, blockUserIds);
    }

    public List<QuestionHowabout> getWaitQuestionHowabout(User user, Long questionId, List<Long> blockUserIds) {
        return questionRepository.getWaitQuestionHowabout(user, questionId, blockUserIds);
    }

    public List<QuestionFind> getWaitQuestionFind(User user, Long questionId, List<Celeb> interestedCelebs, List<Long> blockUserIds) {
        return questionRepository.getWaitQuestionFind(user, questionId, interestedCelebs, blockUserIds);
    }

    public Page<Question> getTotalQuestionList(List<Long> blockUserIds, Pageable pageable) {
        return questionRepository.getTotalQuestionList(blockUserIds, pageable);
    }

    public Page<QuestionBuy> getQuestionBuyList(String voteStatus, List<Long> blockUserIds, Pageable pageable) {
        return questionRepository.getQuestionBuyList(voteStatus, blockUserIds, pageable);
    }

    public Page<QuestionFind> getQuestionFindList(Long celebId, Boolean isNewCeleb, List<Long> blockUserIds, Pageable pageable) {
        return questionRepository.getQuestionFindList(celebId, isNewCeleb, blockUserIds, pageable);
    }

    public Page<QuestionHowabout> getQuestionHowaboutList(List<Long> blockUserIds, Pageable pageable) {
        return questionRepository.getQuestionHowaboutList(blockUserIds, pageable);
    }

    public Page<QuestionRecommend> getQuestionRecommendList(String hashtag, List<Long> blockUserIds, Pageable pageable) {
        return questionRepository.getQuestionRecommendList(hashtag, blockUserIds, pageable);
    }

    public List<Question> getDailyHotQuestion(List<Long> blockUserIds) {
        return questionRepository.getDailyHotQuestion(blockUserIds);
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

    public Page<Question> getWeeklyHotQuestion(List<Long> blockUserIds, Pageable pageable) {
        return questionRepository.getWeeklyHotQuestion(blockUserIds, pageable);
    }

    public void changeAllNewCelebToCeleb(Celeb celeb, Long newCelebId) {
        questionRepository.changeAllNewCelebToCeleb(celeb, newCelebId);
    }
}
