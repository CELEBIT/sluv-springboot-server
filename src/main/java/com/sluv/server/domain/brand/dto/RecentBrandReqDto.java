package com.sluv.server.domain.brand.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecentBrandReqDto {
    private Long brandId;
    private Long newBrandId;
}
