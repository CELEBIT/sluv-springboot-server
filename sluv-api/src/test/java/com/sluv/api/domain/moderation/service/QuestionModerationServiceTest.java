package com.sluv.api.domain.moderation.service;

import com.sluv.api.moderation.service.QuestionModerationService;
import com.sluv.domain.featureflag.enums.FeatureFlagKey;
import com.sluv.domain.featureflag.service.FeatureFlagDomainService;
import com.sluv.domain.moderation.service.ModerationJobDomainService;
import com.sluv.domain.question.entity.QuestionHowabout;
import com.sluv.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionModerationServiceTest {

    @InjectMocks
    private QuestionModerationService questionModerationService;

    @Mock
    private FeatureFlagDomainService featureFlagDomainService;

    @Mock
    private ModerationJobDomainService moderationJobDomainService;

    @Test
    @DisplayName("MODERATION_JOB_CREATION이 true면 질문 검수 작업 생성을 요청한다.")
    void createQuestionJobIfFeatureFlagEnabledTest() {
        // given
        User user = User.builder().id(10L).build();
        QuestionHowabout question = QuestionHowabout.builder()
                .id(1L)
                .user(user)
                .title("질문")
                .build();

        when(featureFlagDomainService.isEnabled(FeatureFlagKey.MODERATION_JOB_CREATION))
                .thenReturn(true);

        // when
        questionModerationService.createQuestionJobIfEnabled(question);

        // then
        verify(featureFlagDomainService).isEnabled(FeatureFlagKey.MODERATION_JOB_CREATION);
        verify(moderationJobDomainService).createQuestionJobIfAbsent(question.getId(), user.getId());
    }

    @Test
    @DisplayName("MODERATION_JOB_CREATION이 false면 질문 검수 작업 생성을 요청하지 않는다.")
    void doesNotCreateQuestionJobIfFeatureFlagDisabledTest() {
        // given
        User user = User.builder().id(10L).build();
        QuestionHowabout question = QuestionHowabout.builder()
                .id(1L)
                .user(user)
                .title("질문")
                .build();

        when(featureFlagDomainService.isEnabled(FeatureFlagKey.MODERATION_JOB_CREATION))
                .thenReturn(false);

        // when
        questionModerationService.createQuestionJobIfEnabled(question);

        // then
        verify(featureFlagDomainService).isEnabled(FeatureFlagKey.MODERATION_JOB_CREATION);
        verify(moderationJobDomainService, never()).createQuestionJobIfAbsent(
                anyLong(),
                anyLong()
        );
    }
}
