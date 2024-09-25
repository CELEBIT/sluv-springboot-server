package com.sluv.domain.question.dto;

import com.sluv.domain.item.entity.ItemImg;
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
public class QuestionImgSimpleDto {
    @Schema(description = "이미지 URL")
    private String imgUrl;
    @Schema(description = "이미지 순서")
    private Long sortOrder;

    public static QuestionImgSimpleDto of(QuestionImg questionImg) {
        return QuestionImgSimpleDto.builder()
                .imgUrl(questionImg.getImgUrl())
                .sortOrder((long) questionImg.getSortOrder())
                .build();
    }

    public static QuestionImgSimpleDto of(ItemImg itemImg) {
        return QuestionImgSimpleDto.builder()
                .imgUrl(itemImg.getItemImgUrl())
                .sortOrder((long) itemImg.getSortOrder())
                .build();
    }
}
