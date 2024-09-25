package com.sluv.api.question.dto;

import com.sluv.domain.question.entity.QuestionImg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionImgResDto {
    @Schema(description = "Question Img URL")
    private String imgUrl;
    @Schema(description = "Question Img 설명")
    private String description;
    @Schema(description = "투표 수")
    private Long voteNum;
    @Schema(description = "투표 퍼센트")
    private Double votePercent;
    @Schema(description = "대표여부")
    private Boolean representFlag;
    @Schema(description = "순서")
    private Integer sortOrder;

    public static QuestionImgResDto of(QuestionImg questionImg, QuestionVoteDataDto voteDataDto) {
        return QuestionImgResDto.builder()
                .imgUrl(questionImg.getImgUrl())
                .description(questionImg.getDescription())
                .voteNum(
                        voteDataDto != null
                                ? voteDataDto.getVoteNum()
                                : null
                )
                .votePercent(
                        voteDataDto != null
                                ? voteDataDto.getVotePercent()
                                : null
                )
                .representFlag(questionImg.getRepresentFlag())
                .sortOrder(questionImg.getSortOrder())
                .build();
    }
}
