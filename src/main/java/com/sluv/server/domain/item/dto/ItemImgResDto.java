package com.sluv.server.domain.item.dto;

import com.sluv.server.domain.item.entity.ItemImg;
import com.sluv.server.domain.item.entity.TempItemImg;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemImgResDto implements Serializable {
    @Schema(description = "아이템 이미지 URL")
    private String imgUrl;
    @Schema(description = "대표 이미지 여부(true or false)")
    private Boolean representFlag;
    @Schema(description = "이미지 순서")
    private Integer sortOrder;

    public static ItemImgResDto of(ItemImg itemImg) {
        return ItemImgResDto.builder()
                .imgUrl(itemImg.getItemImgUrl())
                .representFlag(itemImg.getRepresentFlag())
                .sortOrder(itemImg.getSortOrder())
                .build();
    }

    public static ItemImgResDto of(TempItemImg tempItemImg) {
        return ItemImgResDto.builder()
                .imgUrl(tempItemImg.getTempItemImgUrl())
                .representFlag(tempItemImg.getRepresentFlag())
                .sortOrder(tempItemImg.getSortOrder())
                .build();
    }

}
