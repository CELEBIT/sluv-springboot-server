package com.sluv.api.item.dto;

import com.sluv.domain.item.entity.ItemCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
                                                   List<ItemCategoryChildResponseDto> subCategoryList) {

        return ItemCategoryParentResponseDto.builder()
                .id(itemCategory.getId())
                .name(itemCategory.getName())
                .subCategoryList(subCategoryList)
                .build();
    }
}
