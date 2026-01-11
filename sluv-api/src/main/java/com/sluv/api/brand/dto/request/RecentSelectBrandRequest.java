package com.sluv.api.brand.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecentSelectBrandRequest {
    @Schema(description = "브랜드 Id")
    private Long brandId;
    @Schema(description = "뉴브랜드 Id")
    private Long newBrandId;
}
