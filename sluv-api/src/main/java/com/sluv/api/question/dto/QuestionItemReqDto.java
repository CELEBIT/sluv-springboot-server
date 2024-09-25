package com.sluv.api.question.dto;

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
    @Schema(description = "Question item 설명")
    private String description;
    //    @Schema(description = "Question item 투표수")
//    private Long vote;
    @Schema(description = "대표 여부")
    private Boolean representFlag;
    @Schema(description = "Item 순서")
    private Integer sortOrder;
}
