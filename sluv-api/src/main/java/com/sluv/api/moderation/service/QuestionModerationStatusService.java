package com.sluv.api.moderation.service;

import com.sluv.domain.featureflag.enums.FeatureFlagKey;
import com.sluv.domain.featureflag.service.FeatureFlagDomainService;
import com.sluv.domain.question.enums.QuestionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionModerationStatusService {

    private final FeatureFlagDomainService featureFlagDomainService;

    public QuestionStatus getInitialQuestionStatus() {
        if (featureFlagDomainService.isEnabled(FeatureFlagKey.MODERATION_QUESTION_CREATE_PENDING)) {
            return QuestionStatus.PENDING;
        }

        return QuestionStatus.ACTIVE;
    }

    public QuestionStatus getUpdateQuestionStatus() {
        if (featureFlagDomainService.isEnabled(FeatureFlagKey.MODERATION_QUESTION_UPDATE_PENDING)) {
            return QuestionStatus.PENDING;
        }

        return QuestionStatus.ACTIVE;
    }
}
