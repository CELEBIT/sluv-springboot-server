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
public class CommentReportDetailResponse {

    private Long reporterId;
    private String reporterNickname;
    private Long reportedId;
    private String reportedNickname;
    private Long reportId;
    private CommentReportReason reportReason;
    @Schema(description = "신고 상세 내용")
    private String content;
    @Schema(description = "신고 접수 상태")
    private ReportStatus reportStatus;
    @Schema(description = "신고 당한 원본 댓글 내용")
    private String reportedCommentContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentReportDetailResponse of(CommentReport commentReport, Comment comment) {
        return CommentReportDetailResponse.builder()
                .reporterId(commentReport.getReporter().getId())
                .reporterNickname(commentReport.getReporter().getNickname())
                .reportedId(comment.getUser().getId())
                .reportedNickname(comment.getUser().getNickname())
                .reportReason(commentReport.getCommentReportReason())
                .content(commentReport.getContent())
                .reportStatus(commentReport.getReportStatus())
                .reportedCommentContent(comment.getContent())
                .createdAt(commentReport.getCreatedAt())
                .updatedAt(commentReport.getUpdatedAt())
                .build();
    }

}
