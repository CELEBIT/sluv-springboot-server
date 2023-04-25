package com.sluv.server.domain.celeb.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({"id", "CelebNameKr", "CelebNameEn"})
public class CelebSearchResDto {
    @Schema(description = "Celeb id")
    private Long id;
    @Schema(description = "Celeb 카테고리")
    private String category;
    @Schema(description = "Celeb 한글 이름")
    private String CelebNameKr;
    @Schema(description = "Celeb 영어 이름")
    private String CelebNameEn;

    @Builder
    public CelebSearchResDto(Long id, String category, String celebNameKr, String celebNameEn) {
        this.id = id;
        this.category = category;
        CelebNameKr = celebNameKr;
        CelebNameEn = celebNameEn;
    }
}
