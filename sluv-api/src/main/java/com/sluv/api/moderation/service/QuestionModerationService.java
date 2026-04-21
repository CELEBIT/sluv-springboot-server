package com.sluv.api.moderation.service;

import com.sluv.domain.featureflag.enums.FeatureFlagKey;
import com.sluv.domain.featureflag.service.FeatureFlagDomainService;
import com.sluv.domain.moderation.service.ModerationJobDomainService;
import com.sluv.domain.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionModerationService {

    private final FeatureFlagDomainService featureFlagDomainService;
    private final ModerationJobDomainService moderationJobDomainService;

    public void createQuestionJobIfEnabled(Question question) {
        if (!featureFlagDomainService.isEnabled(FeatureFlagKey.MODERATION_JOB_CREATION)) {
            return;
        }

        moderationJobDomainService.createQuestionJobIfAbsent(question.getId(), question.getUser().getId());
    }
}
