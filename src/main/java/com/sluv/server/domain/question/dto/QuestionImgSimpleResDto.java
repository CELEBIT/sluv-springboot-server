package com.sluv.server.domain.question.dto;

import com.sluv.server.domain.item.entity.ItemImg;
import com.sluv.server.domain.question.entity.QuestionImg;
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

    public static QuestionImgSimpleResDto of(QuestionImg questionImg){
        return QuestionImgSimpleResDto.builder()
                .imgUrl(questionImg.getImgUrl())
                .sortOrder( (long) questionImg.getSortOrder())
                .build();
    }

    public static QuestionImgSimpleResDto of(ItemImg itemImg){
        return QuestionImgSimpleResDto.builder()
                .imgUrl(itemImg.getItemImgUrl())
                .sortOrder( (long) itemImg.getSortOrder())
                .build();
    }
}
