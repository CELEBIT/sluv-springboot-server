package com.sluv.server.domain.question.dto;

import com.sluv.server.domain.question.enums.QuestionReportReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionReportReqDto {
    private QuestionReportReason reason;
    private String content;
}
