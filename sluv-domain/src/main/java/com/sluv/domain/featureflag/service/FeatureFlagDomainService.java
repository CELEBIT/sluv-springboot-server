package com.sluv.domain.featureflag.service;

import com.sluv.domain.featureflag.entity.FeatureFlag;
import com.sluv.domain.featureflag.enums.FeatureFlagKey;
import com.sluv.domain.featureflag.repository.FeatureFlagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureFlagDomainService {

    private final FeatureFlagRepository featureFlagRepository;

    public boolean isEnabled(FeatureFlagKey flagKey) {
        return featureFlagRepository.findByFlagKey(flagKey)
                .map(FeatureFlag::isEnabled)
                .orElseGet(() -> getDefaultValue(flagKey));
    }

    private boolean getDefaultValue(FeatureFlagKey flagKey) {
        return switch (flagKey) {
            case MODERATION_JOB_CREATION -> true;
            case MODERATION_QUESTION_CREATE_PENDING, MODERATION_AUTO_APPLY_RESULT -> false;
        };
    }
}
