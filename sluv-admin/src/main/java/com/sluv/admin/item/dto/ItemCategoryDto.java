package com.sluv.admin.item.dto;

import com.sluv.domain.item.entity.ItemCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemCategoryDto {
    @Schema(description = "아이템 카테고리 Id")
    private Long id;
    @Schema(description = "아이템 상위 카테고리 Id")
    private Long parentId;
    @Schema(description = "아이템 카테고리 이름")
    private String name;
    @Schema(description = "아이템 상위 카테고리 이름")
    private String parentName;

    public static ItemCategoryDto from(ItemCategory itemCategory) {
        return ItemCategoryDto.builder()
                .id(itemCategory.getId())
                .name(itemCategory.getName())
                .parentId(
                        itemCategory.getParent() != null
                                ? itemCategory.getParent().getId()
                                : null
                )
                .parentName(
                        itemCategory.getParent() != null
                                ? itemCategory.getParent().getName()
                                : null
                )
                .build();
    }
}