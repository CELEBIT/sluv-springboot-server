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
    @Schema(description = "Parent Celeb id")
    private Long parentId;
    @Schema(description = "Celeb 카테고리")
    private String category;
    @Schema(description = "Celeb Parent 한글 이름")
    private String celebParentNameKr;
    @Schema(description = "Celeb Child 한글 이름")
    private String celebChildNameKr;
    @Schema(description = "total 한글 이름")
    private String celebTotalNameKr;
    @Schema(description = "total 영어 이름")
    private String celebTotalNameEn;


    @Builder
    public CelebSearchResDto(Long id, Long parentId, String category, String celebParentNameKr, String celebChildNameKr, String celebTotalNameKr, String celebTotalNameEn) {
        this.id = id;
        this.parentId = parentId;
        this.category = category;
        this.celebParentNameKr = celebParentNameKr;
        this.celebChildNameKr = celebChildNameKr;
        this.celebTotalNameKr = celebTotalNameKr;
        this.celebTotalNameEn = celebTotalNameEn;
    }
}
