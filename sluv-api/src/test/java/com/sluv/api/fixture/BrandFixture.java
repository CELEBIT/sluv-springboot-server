package com.sluv.api.fixture;

import com.sluv.domain.brand.entity.Brand;

public class BrandFixture {

    public static Brand 브랜드_생성(String krName, String enName) {
        return Brand.builder()
                .brandKr(krName)
                .brandEn(enName)
                .build();
    }

}
