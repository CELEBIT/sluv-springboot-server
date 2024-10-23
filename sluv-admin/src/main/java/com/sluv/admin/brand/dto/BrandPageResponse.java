package com.sluv.admin.brand.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandPageResponse {

    private Integer pageNumber;
    private Integer totalPageSize;
    private List<BrandResponse> content;

    public static BrandPageResponse of(Integer pageNumber, Integer totalPageSize, List<BrandResponse> content) {
        return BrandPageResponse.builder()
                .pageNumber(pageNumber + 1)
                .totalPageSize(totalPageSize)
                .content(content)
                .build();
    }

}
