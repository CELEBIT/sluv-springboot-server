package com.sluv.api.question.service;

import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.service.QuestionDomainService;
import com.sluv.domain.question.service.QuestionLikeDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.infra.alarm.service.QuestionAlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionLikeService {

    private final UserDomainService userDomainService;
    private final QuestionDomainService questionDomainService;
    private final QuestionLikeDomainService questionLikeDomainService;
    private final QuestionAlarmService questionAlarmService;

    @Transactional
    public void postQuestionLike(Long userId, Long questionId) {
        User user = userDomainService.findById(userId);
        log.info("질문 게시글 좋아요 - 사용자 : {}, 질문 게시글 : {}", user.getId(), questionId);

        Boolean likeStatus = questionLikeDomainService.existsByQuestionIdAndUserId(questionId, user.getId());
        Question question = questionDomainService.findById(questionId);

        if (likeStatus) {
            questionLikeDomainService.deleteByQuestionIdAndUserId(questionId, user.getId());
        } else {
            questionLikeDomainService.saveQuestionLike(user, question);
            questionAlarmService.sendAlarmAboutQuestionLike(user.getId(), question.getId());
        }
    }
}
