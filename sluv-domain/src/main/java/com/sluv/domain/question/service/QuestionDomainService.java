package com.sluv.domain.question.service;

import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.QuestionBuy;
import com.sluv.domain.question.entity.QuestionFind;
import com.sluv.domain.question.entity.QuestionHowabout;
import com.sluv.domain.question.entity.QuestionRecommend;
import com.sluv.domain.question.enums.QuestionStatus;
import com.sluv.domain.question.exception.QuestionNotFoundException;
import com.sluv.domain.question.repository.QuestionRepository;
import com.sluv.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionDomainService {

    private final QuestionRepository questionRepository;


    @Transactional(readOnly = true)
    public Question findById(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);
    }

    @Transactional
    public Page<Question> getUserLikeQuestion(User user, Pageable pageable) {
        return questionRepository.getUserLikeQuestion(user, pageable);
    }

    @Transactional(readOnly = true)
    public Long countByUserIdAndQuestionStatus(Long questionId, QuestionStatus questionStatus) {
        return questionRepository.countByUserIdAndQuestionStatus(questionId, questionStatus);
    }

    @Transactional(readOnly = true)
    public Page<Question> getUserAllQuestion(User user, Pageable pageable) {
        return questionRepository.getUserAllQuestion(user, pageable);
    }

    @Transactional
    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Transactional(readOnly = true)
    public List<QuestionBuy> getWaitQuestionBuy(User user, Long questionId, List<Celeb> interestedCelebs) {
        return questionRepository.getWaitQuestionBuy(user, questionId, interestedCelebs);
    }

    @Transactional(readOnly = true)
    public List<QuestionRecommend> getWaitQuestionRecommend(User user, Long questionId) {
        return questionRepository.getWaitQuestionRecommend(user, questionId);
    }

    @Transactional(readOnly = true)
    public List<QuestionHowabout> getWaitQuestionHowabout(User user, Long questionId) {
        return questionRepository.getWaitQuestionHowabout(user, questionId);
    }

    @Transactional(readOnly = true)
    public List<QuestionFind> getWaitQuestionFind(User user, Long questionId, List<Celeb> interestedCelebs) {
        return questionRepository.getWaitQuestionFind(user, questionId, interestedCelebs);
    }

    @Transactional(readOnly = true)
    public Page<Question> getTotalQuestionList(Pageable pageable) {
        return questionRepository.getTotalQuestionList(pageable);
    }

    @Transactional(readOnly = true)
    public Page<QuestionBuy> getQuestionBuyList(String voteStatus, Pageable pageable) {
        return questionRepository.getQuestionBuyList(voteStatus, pageable);
    }

    @Transactional(readOnly = true)
    public Page<QuestionFind> getQuestionFindList(Long celebId, Pageable pageable) {
        return questionRepository.getQuestionFindList(celebId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<QuestionHowabout> getQuestionHowaboutList(Pageable pageable) {
        return questionRepository.getQuestionHowaboutList(pageable);
    }

    @Transactional(readOnly = true)
    public Page<QuestionRecommend> getQuestionRecommendList(String hashtag, Pageable pageable) {
        return questionRepository.getQuestionRecommendList(hashtag, pageable);
    }

    @Transactional(readOnly = true)
    public List<Question> getDailyHotQuestion() {
        return questionRepository.getDailyHotQuestion();
    }

    @Transactional(readOnly = true)
    public Page<?> getSearchQuestion(List<Long> searchQuestionIds, Pageable pageable) {
        return questionRepository.getSearchQuestion(searchQuestionIds, pageable);
    }

    @Transactional(readOnly = true)
    public Page<?> getSearchQuestionBuy(List<Long> searchQuestionIds, Pageable pageable) {
        return questionRepository.getSearchQuestionBuy(searchQuestionIds, pageable);
    }

    @Transactional(readOnly = true)
    public Page<?> getSearchQuestionFind(List<Long> searchQuestionIds, Pageable pageable) {
        return questionRepository.getSearchQuestionFind(searchQuestionIds, pageable);
    }

    @Transactional(readOnly = true)
    public Page<?> getSearchQuestionHowabout(List<Long> searchQuestionIds, Pageable pageable) {
        return questionRepository.getSearchQuestionHowabout(searchQuestionIds, pageable);
    }

    @Transactional(readOnly = true)
    public Page<?> getSearchQuestionRecommend(List<Long> searchQuestionIds, Pageable pageable) {
        return questionRepository.getSearchQuestionRecommend(searchQuestionIds, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Question> getWeeklyHotQuestion(Pageable pageable) {
        return questionRepository.getWeeklyHotQuestion(pageable);
    }
}
