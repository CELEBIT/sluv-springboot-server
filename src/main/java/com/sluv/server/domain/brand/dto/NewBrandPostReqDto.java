package com.sluv.server.domain.brand.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewBrandPostReqDto {
    @Schema(description = "새로 등록할 브랜드의 이름")
    private String newBrandName;
}
