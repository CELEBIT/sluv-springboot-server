package com.sluv.server.domain.brand.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecentSelectBrandReqDto {
    @Schema(description = "브랜드 Id")
    private Long brandId;
    @Schema(description = "뉴브랜드 Id")
    private Long newBrandId;
}
