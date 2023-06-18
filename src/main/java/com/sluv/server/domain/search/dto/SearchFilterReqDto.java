package com.sluv.server.domain.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchFilterReqDto {
    private Long categoryId;
    private Long minPrice;
    private Long maxPrice;
    private String color;
}
