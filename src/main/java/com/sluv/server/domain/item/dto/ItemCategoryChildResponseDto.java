package com.sluv.server.domain.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class ItemCategoryChildResponseDto {

    private Long id;
    private String name;


    @Builder
    public ItemCategoryChildResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
