package com.sluv.server.domain.brand.dto;

import com.sluv.server.domain.brand.entity.NewBrand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewBrandPostResDto {
    @Schema(description = "생성된 newBrand의 Id")
    private Long newBrandId;
    @Schema(description = "생성된 newBrand의 이름")
    private String newBrandName;

    public static NewBrandPostResDto of(NewBrand newBrand){
        return NewBrandPostResDto.builder()
                .newBrandId(newBrand.getId())
                .newBrandName(newBrand.getBrandName())
                .build();
    }
}
