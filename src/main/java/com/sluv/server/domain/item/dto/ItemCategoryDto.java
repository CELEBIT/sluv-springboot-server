package com.sluv.server.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemCategoryDto {
    private Long id;
    private String name;
    private String parentName;
}
