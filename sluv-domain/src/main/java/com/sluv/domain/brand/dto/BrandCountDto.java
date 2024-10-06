package com.sluv.domain.brand.dto;

import com.sluv.domain.brand.entity.Brand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandCountDto {

    private Brand brand;
    private Long useCount;

    public static BrandCountDto of(Brand brand, Long useCount) {
        return BrandCountDto.builder()
                .brand(brand)
                .useCount(useCount)
                .build();
    }

}
