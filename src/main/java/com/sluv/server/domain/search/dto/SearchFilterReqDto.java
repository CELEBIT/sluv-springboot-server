package com.sluv.server.domain.search.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchFilterReqDto {
    private Long categoryId;
    private Long minPrice;
    private Long maxPrice;
    private String color;
}
