package com.sluv.server.domain.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
public class ItemCategoryParentResponseDto {

    private Long id;
    private String name;

    private List<ItemCategoryChildResponseDto> subCategoryList;

    @Builder
    public ItemCategoryParentResponseDto(Long id, String name, List<ItemCategoryChildResponseDto> subCategoryList) {
        this.id = id;
        this.name = name;
        this.subCategoryList = subCategoryList;
    }
}
