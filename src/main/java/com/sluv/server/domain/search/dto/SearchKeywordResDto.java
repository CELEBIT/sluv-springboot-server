package com.sluv.server.domain.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchKeywordResDto {
    @Schema(description = "키워드")
    private String keyword;

    public static SearchKeywordResDto of(String keyword) {
        return SearchKeywordResDto.builder()
                .keyword(keyword)
                .build();
    }
}
