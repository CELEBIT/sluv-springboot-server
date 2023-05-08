package com.sluv.server.domain.brand.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BrandSearchResDto{
    @Schema(description = "브랜드 Id")
    private Long id;
    @Schema(description = "브랜드 한글이름")
    private String brandKr;
    @Schema(description = "브랜드 영어이름")
    private String brandEn;
    @Schema(description = "브랜드 이미지 URL")
    private String brandImgUrl;

    @Builder
    public BrandSearchResDto(Long id, String brandKr, String brandEn, String brandImgUrl) {
        this.id = id;
        this.brandKr = brandKr;
        this.brandEn = brandEn;
        this.brandImgUrl = brandImgUrl;
    }
}
