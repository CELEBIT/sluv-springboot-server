package com.sluv.server.domain.item.dto;

import com.sluv.server.domain.item.entity.ItemCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCategoryChildResponseDto {

    @Schema(description = "하위 카테고리 Id")
    private Long id;
    @Schema(description = "하위 카테고리 이름")
    private String name;

    public static ItemCategoryChildResponseDto of(ItemCategory itemCategory){
        return ItemCategoryChildResponseDto.builder()
                .id(itemCategory.getId())
                .name(itemCategory.getName())
                .build();
    }
}
