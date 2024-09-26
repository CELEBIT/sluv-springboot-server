package com.sluv.domain.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchFilterReqDto {
    @Schema(description = "카테고리 Id, Nullable")
    private Long categoryId;
    @Schema(description = "최소 금액, Nullable")
    private Long minPrice;
    @Schema(description = "최대 금액, Nullable")
    private Long maxPrice;
    @Schema(description = "색 코드, Nullable")
    private String color;
}
