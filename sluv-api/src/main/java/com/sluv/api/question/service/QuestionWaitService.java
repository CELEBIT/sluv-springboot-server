package com.sluv.api.question.service;

import com.sluv.api.question.helper.QuestionResponseHelper;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.service.CelebDomainService;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.service.QuestionDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserBlockDomainService;
import com.sluv.domain.user.service.UserDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionWaitService {

    private final QuestionDomainService questionDomainService;
    private final UserDomainService userDomainService;
    private final UserBlockDomainService userBlockDomainService;
    private final CelebDomainService celebDomainService;
    private final QuestionResponseHelper questionResponseHelper;

    @Transactional(readOnly = true)
    public List<QuestionSimpleResDto> getWaitQuestionBuy(Long userId, Long questionId) {
        User user = userDomainService.findById(userId);
        List<Long> blockedUserIds = getBlockedUserIds(userId);
        List<Celeb> interestedCelebs = getInterestedCelebs(user);

        return questionDomainService.getWaitQuestionBuy(user, questionId, interestedCelebs, blockedUserIds).stream()
                .map(questionResponseHelper::getQuestionSimpleResponseWithMainImage)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<QuestionSimpleResDto> getWaitQuestionFind(Long userId, Long questionId) {
        User user = userDomainService.findById(userId);
        List<Long> blockedUserIds = getBlockedUserIds(userId);
        List<Celeb> interestedCelebs = getInterestedCelebs(user);

        return questionDomainService.getWaitQuestionFind(user, questionId, interestedCelebs, blockedUserIds).stream()
                .map(questionResponseHelper::getQuestionSimpleResponseWithMainImage)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<QuestionSimpleResDto> getWaitQuestionHowabout(Long userId, Long questionId) {
        User user = userDomainService.findById(userId);
        List<Long> blockedUserIds = getBlockedUserIds(userId);

        return questionDomainService.getWaitQuestionHowabout(user, questionId, blockedUserIds).stream()
                .map(questionResponseHelper::getQuestionSimpleResponseWithMainImage)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<QuestionSimpleResDto> getWaitQuestionRecommend(Long userId, Long questionId) {
        User user = userDomainService.findById(userId);
        List<Long> blockedUserIds = getBlockedUserIds(userId);

        return questionDomainService.getWaitQuestionRecommend(user, questionId, blockedUserIds).stream()
                .map(questionResponseHelper::getQuestionSimpleResponseWithMainImage)
                .toList();
    }

    private List<Long> getBlockedUserIds(Long userId) {
        return userBlockDomainService.getAllBlockedUser(userId).stream()
                .map(userBlock -> userBlock.getBlockedUser().getId())
                .toList();
    }

    private List<Celeb> getInterestedCelebs(User user) {
        if (user == null) {
            return List.of();
        }

        return celebDomainService.findInterestedCeleb(user);
    }
}
