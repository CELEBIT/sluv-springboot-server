package com.sluv.admin.item.dto;

import com.sluv.domain.item.entity.ItemImg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemImgResDto {
    @Schema(description = "아이템 이미지 URL")
    private String imgUrl;
    @Schema(description = "대표 이미지 여부(true or false)")
    private Boolean representFlag;
    @Schema(description = "이미지 순서")
    private Integer sortOrder;

    public static ItemImgResDto from(ItemImg itemImg) {
        return ItemImgResDto.builder()
                .imgUrl(itemImg.getItemImgUrl())
                .representFlag(itemImg.getRepresentFlag())
                .sortOrder(itemImg.getSortOrder())
                .build();
    }
}