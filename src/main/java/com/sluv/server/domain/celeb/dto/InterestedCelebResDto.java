package com.sluv.server.domain.celeb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestedCelebResDto {
    @Schema(description = "Celeb Id")
    private Long id;
    @Schema(description = "Celeb 한글 이름")
    private String celebNameKr;
    @Schema(description = "Celeb 카테고리 이름")
    private String celebCategory;
}
