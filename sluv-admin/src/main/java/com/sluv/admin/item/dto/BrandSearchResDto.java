package com.sluv.admin.item.dto;

import com.sluv.domain.brand.entity.Brand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandSearchResDto {
    @Schema(description = "브랜드 Id")
    private Long id;
    @Schema(description = "브랜드 한글이름")
    private String brandKr;
    @Schema(description = "브랜드 영어이름")
    private String brandEn;
    @Schema(description = "브랜드 이미지 URL")
    private String brandImgUrl;

    public static BrandSearchResDto from(Brand brand) {
        return BrandSearchResDto.builder()
                .id(brand.getId())
                .brandKr(brand.getBrandKr())
                .brandEn(brand.getBrandEn())
                .brandImgUrl(brand.getBrandImgUrl())
                .build();
    }
}