package com.sluv.domain.moderation.repository;

import com.sluv.domain.config.TestConfig;
import com.sluv.domain.moderation.entity.ModerationJob;
import com.sluv.domain.moderation.enums.ModerationJobStatus;
import com.sluv.domain.moderation.enums.ModerationTargetType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
public class ModerationJobRepositoryTest {

    @Autowired
    private ModerationJobRepository moderationJobRepository;

    @AfterEach
    void clean() {
        moderationJobRepository.deleteAll();
    }

    @Test
    @DisplayName("moderation job을 저장한다.")
    void saveModerationJobTest() {
        // given
        ModerationJob moderationJob = createJob(1L, ModerationJobStatus.REQUESTED);

        // when
        ModerationJob savedJob = moderationJobRepository.save(moderationJob);

        // then
        assertThat(savedJob.getId()).isNotNull();
        assertThat(savedJob.getTargetType()).isEqualTo(ModerationTargetType.QUESTION);
        assertThat(savedJob.getTargetId()).isEqualTo(1L);
        assertThat(savedJob.getStatus()).isEqualTo(ModerationJobStatus.REQUESTED);
        assertThat(savedJob.getRequestedBy()).isEqualTo(10L);
    }

    @Test
    @DisplayName("REQUESTED 상태의 moderation job은 open job으로 판단한다.")
    void requestedJobIsOpenJobTest() {
        // given
        moderationJobRepository.save(createJob(1L, ModerationJobStatus.REQUESTED));

        // when
        boolean exists = moderationJobRepository.existsOpenJob(ModerationTargetType.QUESTION, 1L);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("PROCESSING 상태의 moderation job은 open job으로 판단한다.")
    void processingJobIsOpenJobTest() {
        // given
        moderationJobRepository.save(createJob(1L, ModerationJobStatus.PROCESSING));

        // when
        boolean exists = moderationJobRepository.existsOpenJob(ModerationTargetType.QUESTION, 1L);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("NEEDS_REVIEW 상태의 moderation job은 open job으로 판단한다.")
    void needsReviewJobIsOpenJobTest() {
        // given
        moderationJobRepository.save(createJob(1L, ModerationJobStatus.NEEDS_REVIEW));

        // when
        boolean exists = moderationJobRepository.existsOpenJob(ModerationTargetType.QUESTION, 1L);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("완료 상태의 moderation job은 open job으로 판단하지 않는다.")
    void completedJobIsNotOpenJobTest() {
        // given
        moderationJobRepository.save(createJob(1L, ModerationJobStatus.APPROVED));
        moderationJobRepository.save(createJob(1L, ModerationJobStatus.REJECTED));
        moderationJobRepository.save(createJob(1L, ModerationJobStatus.FAILED));
        moderationJobRepository.save(createJob(1L, ModerationJobStatus.CANCELED));

        // when
        boolean exists = moderationJobRepository.existsOpenJob(ModerationTargetType.QUESTION, 1L);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("다른 targetId의 moderation job은 open job으로 판단하지 않는다.")
    void differentTargetIdIsNotOpenJobTest() {
        // given
        moderationJobRepository.save(createJob(1L, ModerationJobStatus.REQUESTED));

        // when
        boolean exists = moderationJobRepository.existsOpenJob(ModerationTargetType.QUESTION, 2L);

        // then
        assertThat(exists).isFalse();
    }

    private ModerationJob createJob(Long targetId, ModerationJobStatus status) {
        return ModerationJob.builder()
                .targetType(ModerationTargetType.QUESTION)
                .targetId(targetId)
                .status(status)
                .requestedBy(10L)
                .reason("테스트")
                .build();
    }
}
