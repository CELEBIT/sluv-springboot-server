package com.sluv.domain.item.dto;

import com.sluv.domain.item.entity.ItemImg;
import com.sluv.domain.item.entity.TempItemImg;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemImgDto implements Serializable {
    @Schema(description = "아이템 이미지 URL")
    private String imgUrl;
    @Schema(description = "대표 이미지 여부(true or false)")
    private Boolean representFlag;
    @Schema(description = "이미지 순서")
    private Integer sortOrder;

    public static ItemImgDto of(ItemImg itemImg) {
        return ItemImgDto.builder()
                .imgUrl(itemImg.getItemImgUrl())
                .representFlag(itemImg.getRepresentFlag())
                .sortOrder(itemImg.getSortOrder())
                .build();
    }

    public static ItemImgDto of(TempItemImg tempItemImg) {
        return ItemImgDto.builder()
                .imgUrl(tempItemImg.getTempItemImgUrl())
                .representFlag(tempItemImg.getRepresentFlag())
                .sortOrder(tempItemImg.getSortOrder())
                .build();
    }

}
