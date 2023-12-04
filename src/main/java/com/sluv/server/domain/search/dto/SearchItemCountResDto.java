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
public class SearchItemCountResDto {
    @Schema(description = "조건에 맞는 아이템의 개수")
    private Long itemCount;

    public static SearchItemCountResDto of(Long itemCount) {
        return SearchItemCountResDto.builder()
                .itemCount(itemCount)
                .build();
    }
}
