package com.sluv.server.domain.brand.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BrandSearchResDto{

    private Long id;
    private String brandKr;
    private String brandEn;
    private String brandImgUrl;

    @Builder
    public BrandSearchResDto(Long id, String brandKr, String brandEn, String brandImgUrl) {
        this.id = id;
        this.brandKr = brandKr;
        this.brandEn = brandEn;
        this.brandImgUrl = brandImgUrl;
    }
}
