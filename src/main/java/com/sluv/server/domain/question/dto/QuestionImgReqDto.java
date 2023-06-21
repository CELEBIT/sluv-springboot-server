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
public class QuestionImgReqDto {
    @Schema(description = "Question img Url")
    private String imgUrl;
    @Schema(description = "Question img 설명")
    private String description;
    @Schema(description = "Question img 투표수")
    private Long vote;
    @Schema(description = "대표 여부")
    private Boolean representFlag;
    @Schema(description = "Img 순서")
    private Integer sortOrder;
}
