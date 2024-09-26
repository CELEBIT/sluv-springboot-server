package com.sluv.domain.user.entity;

import com.sluv.domain.common.entity.BaseEntity;
import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.user.enums.UserReportReason;
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
@Table(name = "user_report")
public class UserReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    @NotNull
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id")
    @NotNull
    private User reported;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserReportReason userReportReason;

    @Size(max = 1002)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'WAITING'")
    private ReportStatus reportStatus;

    public static UserReport toEntity(User reporter, User target, UserReportReason reason, String content) {
        return UserReport.builder()
                .reporter(reporter)
                .reported(target)
                .userReportReason(reason)
                .content(content)
                .reportStatus(ReportStatus.WAITING)
                .build();
    }
}
