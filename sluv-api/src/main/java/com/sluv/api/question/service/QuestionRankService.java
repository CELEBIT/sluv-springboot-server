package com.sluv.api.question.service;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.question.dto.QuestionHomeResDto;
import com.sluv.api.question.helper.QuestionResponseAssembler;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.entity.Question;
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
public class QuestionRankService {

    private final QuestionDomainService questionDomainService;
    private final UserBlockDomainService userBlockDomainService;
    private final QuestionResponseAssembler questionResponseAssembler;

    @Transactional(readOnly = true)
    public List<QuestionHomeResDto> getDailyHotQuestions(Long userId) {
        List<Long> blockedUserIds = getBlockedUserIds(userId);
        List<Question> questions = questionDomainService.getDailyHotQuestion(blockedUserIds);

        return questions.stream()
                .map(questionResponseAssembler::getQuestionHomeResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PaginationResponse<QuestionSimpleResDto> getWeeklyHotQuestions(Long userId, Pageable pageable) {
        List<Long> blockedUserIds = getBlockedUserIds(userId);
        Page<Question> questions = questionDomainService.getWeeklyHotQuestion(blockedUserIds, pageable);

        List<QuestionSimpleResDto> questionResponses = questions.stream()
                .map(questionResponseAssembler::getQuestionSimpleResponseWithImages)
                .toList();

        return PaginationResponse.of(questions, questionResponses);
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
