package com.sluv.server.domain.item.dto;

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
}
