package com.sluv.admin.question.dto;

import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.question.entity.QuestionReport;
import com.sluv.domain.question.enums.QuestionReportReason;
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
public class QuestionReportDetailDto {

    private Long reporterId;
    private String reporterNickname;
    private Long reportedId;
    private String reportedNickname;
    private Long reportId;
    @Schema(description = "질문 신고 이유(enums)")
    private QuestionReportReason reportReason;
    @Schema(description = "신고 상세 내용")
    private String content;
    @Schema(description = "신고 접수 상태")
    private ReportStatus reportStatus;
    @Schema(description = "신고된 질문 제목")
    private String reportedQuestionTitle;
    @Schema(description = "신고된 질문 본문")
    private String reportedQuestionContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static QuestionReportDetailDto from(QuestionReport questionReport) {
        return QuestionReportDetailDto.builder()
                .reporterId(questionReport.getReporter().getId())
                .reporterNickname(questionReport.getReporter().getNickname())
                .reportedId(questionReport.getQuestion().getUser().getId())
                .reportedNickname(questionReport.getQuestion().getUser().getNickname())
                .reportId(questionReport.getId())
                .reportReason(questionReport.getQuestionReportReason())
                .content(questionReport.getContent())
                .reportStatus(questionReport.getReportStatus())
                .reportedQuestionTitle(questionReport.getQuestion().getTitle())
                .reportedQuestionContent(questionReport.getQuestion().getContent())
                .createdAt(questionReport.getCreatedAt())
                .updatedAt(questionReport.getUpdatedAt())
                .build();
    }

}