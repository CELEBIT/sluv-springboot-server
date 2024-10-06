package com.sluv.admin.brand.dto;


import com.sluv.domain.brand.entity.Brand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotBrandResDto {
    private Long id;
    private String name;
    private String imgUrl;
    @Schema(description = "해당 Brand가 사용되는 횟수")
    private Long useCount;

    public static HotBrandResDto of(Brand brand, Long useCount) {
        return HotBrandResDto.builder()
                .id(brand.getId())
                .name(brand.getBrandKr() + " (" + brand.getBrandEn() + ")")
                .imgUrl(brand.getBrandImgUrl())
                .useCount(useCount)
                .build();
    }
}
