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
public class QuestionImgSimpleResDto {
    @Schema(description = "이미지 URL")
    private String imgUrl;
    @Schema(description = "이미지 순서")
    private Long sortOrder;
}
