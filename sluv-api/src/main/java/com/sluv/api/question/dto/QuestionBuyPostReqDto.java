package com.sluv.api.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionBuyPostReqDto {
    @Schema(description = "Question Id (생성: null, 수정: <해당Id>")
    private Long id;
    @Schema(description = "Question 제목")
    private String title;
    @Schema(description = "Question img list")
    private List<QuestionImgReqDto> imgList;
    @Schema(description = "Question item list")
    private List<QuestionItemReqDto> itemList;
    @Schema(description = "Question 투표 종료 시간")
    private LocalDateTime voteEndTime;

}
