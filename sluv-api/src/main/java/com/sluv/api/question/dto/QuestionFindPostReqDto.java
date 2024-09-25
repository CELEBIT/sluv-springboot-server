package com.sluv.api.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionFindPostReqDto {
    @Schema(description = "Question Id (생성: null, 수정: <해당Id>")
    private Long id;
    @Schema(description = "Celeb Id")
    private Long celebId;
    @Schema(description = "NewCeleb Id")
    private Long newCelebId;
    @Schema(description = "Question 제목")
    private String title;
    @Schema(description = "Question 내용")
    private String content;
    @Schema(description = "Question img list")
    private List<QuestionImgReqDto> imgList;
    @Schema(description = "Question item list")
    private List<QuestionItemReqDto> itemList;
}
