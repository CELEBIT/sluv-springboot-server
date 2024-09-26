package com.sluv.api.question.dto;

import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.question.entity.QuestionItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionItemResDto {
    @Schema(description = "Question Item")
    private ItemSimpleDto item;
    @Schema(description = "Question Item 설명")
    private String description;
    @Schema(description = "투표 수")
    private Long voteNum;
    @Schema(description = "투표 퍼센트")
    private Double votePercent;
    @Schema(description = "대표 여부")
    private Boolean representFlag;
    @Schema(description = "순서")
    private Integer sortOrder;

    public static QuestionItemResDto of(QuestionItem questionItem, ItemSimpleDto itemSimpleDto,
                                        QuestionVoteDataDto voteDataDto) {
        return QuestionItemResDto.builder()
                .item(itemSimpleDto)
                .description(questionItem.getDescription())
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
                .representFlag(questionItem.getRepresentFlag())
                .sortOrder(questionItem.getSortOrder())
                .build();
    }
}
