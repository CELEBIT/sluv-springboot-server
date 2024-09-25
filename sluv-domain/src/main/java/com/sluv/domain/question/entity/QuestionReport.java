package com.sluv.domain.question.entity;

import com.sluv.domain.common.entity.BaseEntity;
import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.question.enums.QuestionReportReason;
import com.sluv.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "question_report")
public class QuestionReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    @NotNull
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @NotNull
    private Question question;

    @NotNull
    @Enumerated(EnumType.STRING)
    private QuestionReportReason questionReportReason;

    @Size(max = 1002)
    private String content;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'WAITING'")
    private ReportStatus reportStatus = ReportStatus.WAITING;


    public static QuestionReport toEntity(User user, Question question,
                                          QuestionReportReason reason, String content) {
        return QuestionReport.builder()
                .reporter(user)
                .question(question)
                .questionReportReason(reason)
                .content(content)
                .build();
    }
}
