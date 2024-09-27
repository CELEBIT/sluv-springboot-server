package com.sluv.api.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class QuestionVoteDataDto {
    @Schema(description = "투표 수")
    private Long voteNum;
    @Schema(description = "투표 퍼센트")
    private Double votePercent;

    public static QuestionVoteDataDto of(Long voteNum, Double votePercent) {
        return QuestionVoteDataDto.builder()
                .voteNum(voteNum)
                .votePercent(votePercent)
                .build();
    }
}
