package com.sluv.server.domain.comment.dto;

import com.sluv.server.domain.comment.enums.CommentReportReason;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentReportPostReqDto {
    @Schema(description = "신고 이유")
    private CommentReportReason reason;
    @Schema(description = "신고 내용")
    private String content;
}
