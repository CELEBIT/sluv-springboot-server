package com.sluv.api.brand.dto.response;

import com.sluv.domain.brand.entity.NewBrand;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NewBrandPostResponse implements Serializable {
    @Schema(description = "생성된 newBrand의 Id")
    private Long newBrandId;
    @Schema(description = "생성된 newBrand의 이름")
    private String newBrandName;

    public static NewBrandPostResponse from(NewBrand newBrand) {
        return NewBrandPostResponse.builder()
                .newBrandId(newBrand.getId())
                .newBrandName(newBrand.getBrandName())
                .build();
    }
}
