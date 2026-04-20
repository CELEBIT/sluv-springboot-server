package com.sluv.api.question.service;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.question.helper.QuestionResponseHelper;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.QuestionFind;
import com.sluv.domain.question.entity.QuestionHowabout;
import com.sluv.domain.question.entity.QuestionRecommend;
import com.sluv.domain.question.service.QuestionDomainService;
import com.sluv.domain.user.service.UserBlockDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionFeedService {

    private final QuestionDomainService questionDomainService;
    private final UserBlockDomainService userBlockDomainService;
    private final QuestionResponseHelper questionResponseHelper;

    @Transactional(readOnly = true)
    public PaginationResponse<QuestionSimpleResDto> getTotalQuestions(Long userId, Pageable pageable) {
        List<Long> blockedUserIds = getBlockedUserIds(userId);
        Page<Question> questions = questionDomainService.getTotalQuestionList(blockedUserIds, pageable);

        return questionResponseHelper.getQuestionSimpleResponsesWithMainImage(questions);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<QuestionSimpleResDto> getFindQuestions(Long userId, Long celebId, Boolean isNewCeleb,
                                                                     Pageable pageable) {
        List<Long> blockedUserIds = getBlockedUserIds(userId);
        Page<QuestionFind> questions = questionDomainService.getQuestionFindList(
                celebId,
                isNewCeleb,
                blockedUserIds,
                pageable
        );

        return questionResponseHelper.getQuestionSimpleResponsesWithMainImage(questions);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<QuestionSimpleResDto> getHowaboutQuestions(Long userId, Pageable pageable) {
        List<Long> blockedUserIds = getBlockedUserIds(userId);
        Page<QuestionHowabout> questions = questionDomainService.getQuestionHowaboutList(blockedUserIds, pageable);

        return questionResponseHelper.getQuestionSimpleResponsesWithMainImage(questions);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<QuestionSimpleResDto> getRecommendQuestions(Long userId, String hashtag,
                                                                          Pageable pageable) {
        List<Long> blockedUserIds = getBlockedUserIds(userId);
        Page<QuestionRecommend> questions = questionDomainService.getQuestionRecommendList(
                hashtag,
                blockedUserIds,
                pageable
        );

        return questionResponseHelper.getQuestionSimpleResponsesWithMainImage(questions);
    }

    private List<Long> getBlockedUserIds(Long userId) {
        if (userId == null) {
            return List.of();
        }

        return userBlockDomainService.getAllBlockedUser(userId).stream()
                .map(userBlock -> userBlock.getBlockedUser().getId())
                .toList();
    }

}
