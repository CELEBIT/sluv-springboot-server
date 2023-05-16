package com.sluv.server.domain.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionItemReqDto {
    @Schema(description = "Question item Id")
    private Long itemId;
    @Schema(description = "대표 여부")
    private Boolean representFlag;
}
