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
public class QuestionPostResDto {
    @Schema(description = "생성된 Question의 Id")
    private Long id;

    public static QuestionPostResDto of(Long questionId) {
        return QuestionPostResDto.builder()
                .id(questionId)
                .build();
    }
}
