package com.sluv.server.domain.item.dto;

import com.sluv.server.domain.item.entity.Item;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemKeywordSearchResDto {
    @Schema(description = "아이템 Id")
    private Long itemId;
    @Schema(description = "아이템의 브랜드 이름")
    private String brandName;
    @Schema(description = "아이템의 이름")
    private String itemName;
    @Schema(description = "아이템 상위 카테고리")
    private String parentCategoryName;
    @Schema(description = "아이템 하위 카테고리")
    private String childCategoryName;


    public static ItemKeywordSearchResDto of(Item item) {
        return ItemKeywordSearchResDto.builder()
                .itemId(item.getId())
                .brandName(
                        item.getBrand() != null
                                ? item.getBrand().getBrandKr()
                                : item.getNewBrand().getBrandName()
                )
                .itemName(item.getName())
                .parentCategoryName(item.getCategory().getParent() != null
                        ? item.getCategory().getParent().getName()
                        : null)
                .childCategoryName(item.getCategory().getName())
                .build();
    }
}
