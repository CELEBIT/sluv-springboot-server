package com.sluv.server.domain.item.dto;

import com.sluv.server.domain.item.entity.ItemCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCategoryParentResponseDto {

    @Schema(description = "상위 카테고리 Id")
    private Long id;
    @Schema(description = "상위 카테고리 이름")
    private String name;
    @Schema(description = "하위 카테고리 목록")
    private List<ItemCategoryChildResponseDto> subCategoryList;

    public static ItemCategoryParentResponseDto of(ItemCategory itemCategory,
                                                   List<ItemCategoryChildResponseDto> subCategoryList){

        return ItemCategoryParentResponseDto.builder()
                .id(itemCategory.getId())
                .name(itemCategory.getName())
                .subCategoryList(subCategoryList)
                .build();
    }
}
