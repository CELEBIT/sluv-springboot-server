package com.sluv.server.domain.question.entity;

import com.sluv.server.domain.question.enums.QuestionReportReason;
import com.sluv.server.domain.user.entity.User;
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

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    @NotNull
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @NotNull
    private Question question;

    @NotNull
    @Enumerated(EnumType.STRING)
    private QuestionReportReason questionReportReason;

    @Size(max = 1002)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'WAITING'")
    private ReportStatus reportStatus;

    @Builder
    public QuestionReport(Long id, User reporter, Question question, QuestionReportReason questionReportReason, String content, ReportStatus reportStatus) {
        this.id = id;
        this.reporter = reporter;
        this.question = question;
        this.questionReportReason = questionReportReason;
        this.content = content;
        this.reportStatus = reportStatus;
    }
}
