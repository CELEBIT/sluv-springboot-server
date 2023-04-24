package com.sluv.server.domain.brand.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecentSelectBrandResDto {
    private Long id;
    private String brandName;
    private String flag;

    @Builder
    public RecentSelectBrandResDto(Long id, String brandName, String flag) {
        this.id = id;
        this.brandName = brandName;
        this.flag = flag;
    }
}
