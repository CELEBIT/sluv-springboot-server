package com.sluv.domain.moderation.entity;

import com.sluv.domain.common.entity.BaseEntity;
import com.sluv.domain.moderation.enums.ModerationJobStatus;
import com.sluv.domain.moderation.enums.ModerationTargetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "moderation_job")
public class ModerationJob extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moderation_job_id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
    private ModerationTargetType targetType;

    @NotNull
    @Column(nullable = false)
    private Long targetId;

    @NotNull
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false, columnDefinition = "varchar(45) default 'REQUESTED'")
    private ModerationJobStatus status = ModerationJobStatus.REQUESTED;

    private Long requestedBy;

    private Double score;

    @Size(max = 255)
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String resultPayload;

    private Long reviewedBy;

    private LocalDateTime reviewedAt;

    private LocalDateTime processedAt;

    public static ModerationJob createQuestionJob(Long questionId, Long requestedBy) {
        return ModerationJob.builder()
                .targetType(ModerationTargetType.QUESTION)
                .targetId(questionId)
                .requestedBy(requestedBy)
                .status(ModerationJobStatus.REQUESTED)
                .build();
    }
}
