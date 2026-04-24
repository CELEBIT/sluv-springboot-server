package com.sluv.domain.featureflag.service;

import com.sluv.domain.featureflag.entity.FeatureFlag;
import com.sluv.domain.featureflag.enums.FeatureFlagKey;
import com.sluv.domain.featureflag.repository.FeatureFlagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeatureFlagDomainServiceTest {

    @InjectMocks
    private FeatureFlagDomainService featureFlagDomainService;

    @Mock
    private FeatureFlagRepository featureFlagRepository;

    @Test
    @DisplayName("Feature Flag가 DB에 있으면 DB 값을 반환한다.")
    void isEnabledReturnsDatabaseValueTest() {
        // given
        FeatureFlag featureFlag = FeatureFlag.builder()
                .flagKey(FeatureFlagKey.MODERATION_JOB_CREATION)
                .enabled(false)
                .description("게시글 업로드 시 검수 작업 생성 여부")
                .build();

        when(featureFlagRepository.findByFlagKey(FeatureFlagKey.MODERATION_JOB_CREATION))
                .thenReturn(Optional.of(featureFlag));

        // when
        boolean enabled = featureFlagDomainService.isEnabled(FeatureFlagKey.MODERATION_JOB_CREATION);

        // then
        assertThat(enabled).isFalse();
    }

    @Test
    @DisplayName("MODERATION_JOB_CREATION이 DB에 없으면 기본값 true를 반환한다.")
    void moderationJobCreationDefaultValueTest() {
        // given
        when(featureFlagRepository.findByFlagKey(FeatureFlagKey.MODERATION_JOB_CREATION))
                .thenReturn(Optional.empty());

        // when
        boolean enabled = featureFlagDomainService.isEnabled(FeatureFlagKey.MODERATION_JOB_CREATION);

        // then
        assertThat(enabled).isTrue();
    }

    @Test
    @DisplayName("MODERATION_QUESTION_CREATE_PENDING이 DB에 없으면 기본값 false를 반환한다.")
    void moderationQuestionCreatePendingDefaultValueTest() {
        // given
        when(featureFlagRepository.findByFlagKey(FeatureFlagKey.MODERATION_QUESTION_CREATE_PENDING))
                .thenReturn(Optional.empty());

        // when
        boolean enabled = featureFlagDomainService.isEnabled(FeatureFlagKey.MODERATION_QUESTION_CREATE_PENDING);

        // then
        assertThat(enabled).isFalse();
    }

    @Test
    @DisplayName("MODERATION_AUTO_APPLY_RESULT가 DB에 없으면 기본값 false를 반환한다.")
    void moderationAutoApplyResultDefaultValueTest() {
        // given
        when(featureFlagRepository.findByFlagKey(FeatureFlagKey.MODERATION_AUTO_APPLY_RESULT))
                .thenReturn(Optional.empty());

        // when
        boolean enabled = featureFlagDomainService.isEnabled(FeatureFlagKey.MODERATION_AUTO_APPLY_RESULT);

        // then
        assertThat(enabled).isFalse();
    }

    @Test
    @DisplayName("MODERATION_QUESTION_UPDATE_PENDING이 DB에 없으면 기본값 false를 반환한다.")
    void moderationQuestionUpdatePendingDefaultValueTest() {
        // given
        when(featureFlagRepository.findByFlagKey(FeatureFlagKey.MODERATION_QUESTION_UPDATE_PENDING))
                .thenReturn(Optional.empty());

        // when
        boolean enabled = featureFlagDomainService.isEnabled(FeatureFlagKey.MODERATION_QUESTION_UPDATE_PENDING);

        // then
        assertThat(enabled).isFalse();
    }
}
