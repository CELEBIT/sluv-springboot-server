package com.sluv.server.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionImgResDto {
    private String imgUrl;
    private Boolean representFlag;
    private Integer order;
}
