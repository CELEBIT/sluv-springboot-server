package com.sluv.api.domain.moderation.service;

import com.sluv.api.moderation.service.QuestionModerationStatusService;
import com.sluv.domain.featureflag.enums.FeatureFlagKey;
import com.sluv.domain.featureflag.service.FeatureFlagDomainService;
import com.sluv.domain.question.enums.QuestionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionModerationStatusServiceTest {

    @InjectMocks
    private QuestionModerationStatusService questionModerationStatusService;

    @Mock
    private FeatureFlagDomainService featureFlagDomainService;

    @Test
    @DisplayName("질문 생성 PENDING 플래그가 true면 생성 상태는 PENDING이다.")
    void getInitialQuestionStatusWithPendingFlagEnabledTest() {
        // given
        when(featureFlagDomainService.isEnabled(FeatureFlagKey.MODERATION_QUESTION_CREATE_PENDING))
                .thenReturn(true);

        // when
        QuestionStatus status = questionModerationStatusService.getInitialQuestionStatus();

        // then
        assertThat(status).isEqualTo(QuestionStatus.PENDING);
    }

    @Test
    @DisplayName("질문 생성 PENDING 플래그가 false면 생성 상태는 ACTIVE다.")
    void getInitialQuestionStatusWithPendingFlagDisabledTest() {
        // given
        when(featureFlagDomainService.isEnabled(FeatureFlagKey.MODERATION_QUESTION_CREATE_PENDING))
                .thenReturn(false);

        // when
        QuestionStatus status = questionModerationStatusService.getInitialQuestionStatus();

        // then
        assertThat(status).isEqualTo(QuestionStatus.ACTIVE);
    }

    @Test
    @DisplayName("질문 수정 PENDING 플래그가 true면 수정 상태는 PENDING이다.")
    void getUpdateQuestionStatusWithPendingFlagEnabledTest() {
        // given
        when(featureFlagDomainService.isEnabled(FeatureFlagKey.MODERATION_QUESTION_UPDATE_PENDING))
                .thenReturn(true);

        // when
        QuestionStatus status = questionModerationStatusService.getUpdateQuestionStatus();

        // then
        assertThat(status).isEqualTo(QuestionStatus.PENDING);
    }

    @Test
    @DisplayName("질문 수정 PENDING 플래그가 false면 수정 상태는 ACTIVE다.")
    void getUpdateQuestionStatusWithPendingFlagDisabledTest() {
        // given
        when(featureFlagDomainService.isEnabled(FeatureFlagKey.MODERATION_QUESTION_UPDATE_PENDING))
                .thenReturn(false);

        // when
        QuestionStatus status = questionModerationStatusService.getUpdateQuestionStatus();

        // then
        assertThat(status).isEqualTo(QuestionStatus.ACTIVE);
    }
}
