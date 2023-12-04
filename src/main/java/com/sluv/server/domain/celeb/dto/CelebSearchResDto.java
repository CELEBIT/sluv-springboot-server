package com.sluv.server.domain.celeb.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sluv.server.domain.celeb.entity.Celeb;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public static CelebSearchResDto of(Celeb celeb) {
        return CelebSearchResDto.builder()
                .id(celeb.getId())
                .parentId(
                        celeb.getParent() != null
                                ? celeb.getParent().getId()
                                : null
                )
                .category(
                        celeb.getCelebCategory().getParent() != null
                                ? celeb.getCelebCategory().getParent().getName()
                                : celeb.getCelebCategory().getName()
                )
                .celebParentNameKr(
                        celeb.getParent() != null
                                ? celeb.getParent().getCelebNameKr()
                                : null
                )
                .celebChildNameKr(celeb.getCelebNameKr())
                .celebTotalNameKr(
                        celeb.getParent() != null
                                ? celeb.getParent().getCelebNameKr() + " " + celeb.getCelebNameKr()
                                : celeb.getCelebNameKr()
                )
                .celebTotalNameEn(
                        celeb.getParent() != null
                                ? celeb.getParent().getCelebNameEn() + " " + celeb.getCelebNameEn()
                                : celeb.getCelebNameEn()
                )
                .build();
    }
}
