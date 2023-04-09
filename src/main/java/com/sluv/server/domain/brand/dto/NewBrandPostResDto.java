package com.sluv.server.domain.brand.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewBrandPostResDto {
    @Schema(description = "NewBrand의 Id")
    private Long newBrandId;
    @Schema(description = "NewBrand의 이름")
    private String newBrandName;
}
