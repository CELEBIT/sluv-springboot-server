package com.sluv.api.comment.dto.request;

import com.sluv.domain.comment.enums.CommentReportReason;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentReportPostRequest {
    @Schema(description = "신고 이유")
    private CommentReportReason reason;
    @Schema(description = "신고 내용")
    private String content;
}
