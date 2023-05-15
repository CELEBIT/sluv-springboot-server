package com.sluv.server.domain.brand.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecentSelectBrandResDto {
    @Schema(description = "브랜드 Id")
    private Long id;
    @Schema(description = "브랜드 이름")
    private String brandName;
    @Schema(description = "브랜드 Img Url")
    private String brandImgUrl;
    @Schema(description = "브랜드(Y)와 뉴브랜드(N)를 구분하는 플래그")
    private String flag;

    @Builder
    public RecentSelectBrandResDto(Long id, String brandName, String brandImgUrl, String flag) {
        this.id = id;
        this.brandName = brandName;
        this.brandImgUrl = brandImgUrl;
        this.flag = flag;
    }
}
