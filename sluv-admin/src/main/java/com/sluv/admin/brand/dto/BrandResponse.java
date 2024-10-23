package com.sluv.admin.brand.dto;

import com.sluv.domain.brand.entity.Brand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {

    private Long brandId;
    private String imageUrl;
    private String krName;
    private String enName;

    public static BrandResponse from(Brand brand) {
        return BrandResponse.builder()
                .brandId(brand.getId())
                .imageUrl(brand.getBrandImgUrl())
                .krName(brand.getBrandKr())
                .enName(brand.getBrandEn())
                .build();
    }

}
