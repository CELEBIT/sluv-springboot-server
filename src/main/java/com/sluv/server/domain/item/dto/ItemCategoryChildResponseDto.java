package com.sluv.server.domain.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class ItemCategoryChildResponseDto {

    @Schema(description = "하위 카테고리 Id")
    private Long id;
    @Schema(description = "하위 카테고리 이름")
    private String name;


    @Builder
    public ItemCategoryChildResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
