package com.sluv.server.domain.user.entity;

import com.sluv.server.domain.user.enums.UserReportReason;
import com.sluv.server.domain.user.enums.UserStatus;
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
@Table(name = "user_report")
public class UserReport extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_report_id")
    private Long id;

    @NotNull
    private Long reporterId;

    @NotNull
    private Long reportedId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Size(max = 45)
    private UserReportReason userReportReason;

    @Size(max = 1002)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'WAITING'")
    private ReportStatus reportStatus;


    @Builder
    public UserReport(Long id, Long reporterId, Long reportedId, UserReportReason userReportReason, String content, ReportStatus reportStatus) {
        this.id = id;
        this.reporterId = reporterId;
        this.reportedId = reportedId;
        this.userReportReason = userReportReason;
        this.content = content;
        this.reportStatus = reportStatus;
    }
}
