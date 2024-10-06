package com.sluv.admin.comment.dto;

import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.entity.CommentReport;
import com.sluv.domain.comment.enums.CommentReportReason;
import com.sluv.domain.common.enums.ReportStatus;
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
public class CommentReportInfoDto {

    private Long reporterId;
    private String reporterNickname;
    private Long reportedId;
    private String reportedNickname;
    private Long reportId;
    @Schema(description = "댓글 신고 이유(enums)")
    private CommentReportReason reportReason;
    @Schema(description = "신고 상세 내용")
    private String content;
    @Schema(description = "신고 접수 상태")
    private ReportStatus reportStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentReportInfoDto of(CommentReport commentReport, Comment comment) {
        return CommentReportInfoDto.builder()
                .reporterId(commentReport.getReporter().getId())
                .reporterNickname(commentReport.getReporter().getNickname())
                .reportedId(comment.getUser().getId())
                .reportedNickname(comment.getUser().getNickname())
                .reportId(commentReport.getId())
                .reportReason(commentReport.getCommentReportReason())
                .content(commentReport.getContent())
                .reportStatus(commentReport.getReportStatus())
                .createdAt(commentReport.getCreatedAt())
                .updatedAt(commentReport.getUpdatedAt())
                .build();
    }

}
