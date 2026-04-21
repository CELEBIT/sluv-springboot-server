package com.sluv.domain.moderation.service;

import com.sluv.domain.moderation.entity.ModerationJob;
import com.sluv.domain.moderation.enums.ModerationJobStatus;
import com.sluv.domain.moderation.enums.ModerationTargetType;
import com.sluv.domain.moderation.repository.ModerationJobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ModerationJobDomainServiceTest {

    @InjectMocks
    private ModerationJobDomainService moderationJobDomainService;

    @Mock
    private ModerationJobRepository moderationJobRepository;

    @Test
    @DisplayName("open job이 없으면 REQUESTED 상태의 질문 검수 작업을 생성한다.")
    void createQuestionJobIfOpenJobDoesNotExistTest() {
        // given
        Long questionId = 1L;
        Long requestedBy = 10L;

        when(moderationJobRepository.existsOpenJob(ModerationTargetType.QUESTION, questionId))
                .thenReturn(false);

        // when
        moderationJobDomainService.createQuestionJobIfAbsent(questionId, requestedBy);

        // then
        verify(moderationJobRepository).existsOpenJob(ModerationTargetType.QUESTION, questionId);

        ArgumentCaptor<ModerationJob> captor = ArgumentCaptor.forClass(ModerationJob.class);
        verify(moderationJobRepository).save(captor.capture());

        ModerationJob savedJob = captor.getValue();
        assertThat(savedJob.getTargetType()).isEqualTo(ModerationTargetType.QUESTION);
        assertThat(savedJob.getTargetId()).isEqualTo(questionId);
        assertThat(savedJob.getRequestedBy()).isEqualTo(requestedBy);
        assertThat(savedJob.getStatus()).isEqualTo(ModerationJobStatus.REQUESTED);
    }

    @Test
    @DisplayName("open job이 있으면 질문 검수 작업을 중복 생성하지 않는다.")
    void doesNotCreateQuestionJobIfOpenJobExistsTest() {
        // given
        Long questionId = 1L;
        Long requestedBy = 10L;

        when(moderationJobRepository.existsOpenJob(ModerationTargetType.QUESTION, questionId))
                .thenReturn(true);

        // when
        moderationJobDomainService.createQuestionJobIfAbsent(questionId, requestedBy);

        // then
        verify(moderationJobRepository).existsOpenJob(ModerationTargetType.QUESTION, questionId);
        verify(moderationJobRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
