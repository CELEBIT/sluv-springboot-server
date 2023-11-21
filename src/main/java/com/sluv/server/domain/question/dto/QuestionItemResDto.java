package com.sluv.server.domain.question.dto;

import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.question.entity.QuestionItem;
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
    private ItemSimpleResDto item;
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

    public static QuestionItemResDto of(QuestionItem questionItem, ItemSimpleResDto itemSimpleResDto,
                                        QuestionVoteDataDto voteDataDto) {
        return QuestionItemResDto.builder()
                .item(itemSimpleResDto)
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
