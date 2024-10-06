package com.sluv.admin.user.dto;

import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.user.entity.UserReport;
import com.sluv.domain.user.enums.UserReportReason;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReportInfoDto {

    private Long reporterId;
    private String reporterNickname;
    private Long reportedId;
    private String reportedNickname;
    private Long reportId;
    @Schema(description = "유저 신고 이유(enums)")
    private UserReportReason reportReason;
    @Schema(description = "신고 상세 내용")
    private String content;
    @Schema(description = "신고 접수 상태")
    private ReportStatus reportStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserReportInfoDto from(UserReport userReport) {
        return UserReportInfoDto.builder()
                .reporterId(userReport.getReporter().getId())
                .reporterNickname(userReport.getReporter().getNickname())
                .reportedId(userReport.getReported().getId())
                .reportedNickname(userReport.getReported().getNickname())
                .reportReason(userReport.getUserReportReason())
                .content(userReport.getContent())
                .reportStatus(userReport.getReportStatus())
                .createdAt(userReport.getCreatedAt())
                .updatedAt(userReport.getUpdatedAt())
                .build();
    }

}
