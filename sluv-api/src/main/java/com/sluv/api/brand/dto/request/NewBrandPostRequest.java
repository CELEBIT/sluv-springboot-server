package com.sluv.api.brand.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewBrandPostRequest {
    @Schema(description = "새로 등록할 브랜드의 이름")
    private String newBrandName;
}
