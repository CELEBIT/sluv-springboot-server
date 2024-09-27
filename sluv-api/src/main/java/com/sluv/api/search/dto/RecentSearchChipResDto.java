package com.sluv.api.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentSearchChipResDto {
    @Schema(description = "최근 검색어 키워드")
    private String keyword;

    public static RecentSearchChipResDto of(String keyword) {
        return RecentSearchChipResDto.builder()
                .keyword(keyword)
                .build();
    }
}
