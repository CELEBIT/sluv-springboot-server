package com.sluv.api.brand.dto.response;

import com.sluv.domain.brand.entity.Brand;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BrandSearchResponse implements Serializable {
    @Schema(description = "브랜드 Id")
    private Long id;
    @Schema(description = "브랜드 한글이름")
    private String brandKr;
    @Schema(description = "브랜드 영어이름")
    private String brandEn;
    @Schema(description = "브랜드 이미지 URL")
    private String brandImgUrl;

    public static BrandSearchResponse of(Brand brand) {
        return BrandSearchResponse.builder()
                .id(brand.getId())
                .brandKr(brand.getBrandKr())
                .brandEn(brand.getBrandEn())
                .brandImgUrl(brand.getBrandImgUrl())
                .build();
    }
}
