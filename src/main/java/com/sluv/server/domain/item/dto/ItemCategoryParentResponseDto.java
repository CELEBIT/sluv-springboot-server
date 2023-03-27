package com.sluv.server.domain.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
public class ItemCategoryParentResponseDto {

    @Schema(description = "상위 카테고리 Id")
    private Long id;
    @Schema(description = "상위 카테고리 이름")
    private String name;
    @Schema(description = "하위 카테고리 목록")
    private List<ItemCategoryChildResponseDto> subCategoryList;

    @Builder
    public ItemCategoryParentResponseDto(Long id, String name, List<ItemCategoryChildResponseDto> subCategoryList) {
        this.id = id;
        this.name = name;
        this.subCategoryList = subCategoryList;
    }
}
