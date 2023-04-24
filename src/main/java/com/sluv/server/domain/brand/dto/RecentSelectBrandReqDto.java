package com.sluv.server.domain.brand.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecentSelectBrandReqDto {
    private Long brandId;
    private Long newBrandId;
}
