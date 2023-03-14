package com.sluv.server.domain.question.entity;

import com.sluv.server.domain.question.enums.QuestionReportReason;
import com.sluv.server.domain.user.enums.UserReportReason;
import com.sluv.server.global.common.entity.BaseEntity;
import com.sluv.server.global.common.enums.ReportStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "question_report")
public class QuestionReport extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_report_id")
    private Long id;

    @NotNull
    private Long reporterId;

    @NotNull
    private Long questionId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Size(max = 45)
    private QuestionReportReason questionReportReason;

    @Size(max = 1002)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'WAITING'")
    private ReportStatus reportStatus;

    @Builder
    public QuestionReport(Long id, Long reporterId, Long questionId, QuestionReportReason questionReportReason, String content, ReportStatus reportStatus) {
        this.id = id;
        this.reporterId = reporterId;
        this.questionId = questionId;
        this.questionReportReason = questionReportReason;
        this.content = content;
        this.reportStatus = reportStatus;
    }
}
