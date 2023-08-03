package com.sluv.server.domain.brand.dto;

import com.sluv.server.domain.brand.entity.RecentSelectBrand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentSelectBrandResDto {
    @Schema(description = "브랜드 Id")
    private Long id;
    @Schema(description = "브랜드 이름")
    private String brandName;
    @Schema(description = "브랜드 Img Url")
    private String brandImgUrl;
    @Schema(description = "브랜드(Y)와 뉴브랜드(N)를 구분하는 플래그")
    private String flag;

    public static RecentSelectBrandResDto of(RecentSelectBrand recentSelectBrand){
        Long id;
        String brandName;
        String brandImgUrl;
        String flag;

        if(recentSelectBrand.getBrand() != null){
            id = recentSelectBrand.getBrand().getId();
            brandName = recentSelectBrand.getBrand().getBrandKr();
            brandImgUrl = recentSelectBrand.getBrand().getBrandImgUrl();
            flag = "Y";
        }else{
            id = recentSelectBrand.getNewBrand().getId();
            brandName = recentSelectBrand.getNewBrand().getBrandName();
            brandImgUrl = null;
            flag = "N";
        }

        return RecentSelectBrandResDto.builder()
                .id(id)
                .brandName(brandName)
                .brandImgUrl(brandImgUrl)
                .flag(flag)
                .build();
    }
}
