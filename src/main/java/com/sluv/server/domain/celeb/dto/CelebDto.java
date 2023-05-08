package com.sluv.server.domain.celeb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CelebDto {
    @Schema(description = "셀럽 Id")
    private Long id;
    @Schema(description = "셀럽 한글이름")
    private String celebNameKr;
    @Schema(description = "셀럽 영어이름")
    private String celebNameEn;
    @Schema(description = "셀럽의 하위 카테고리 이름")
    private String categoryChild;
    @Schema(description = "셀럽의 상위 카테고리 이름")
    private String categoryParent;

    @Schema(description = "셀럽의 그룹 한글이름")
    private String parentCelebNameKr;
    @Schema(description = "셀럽의 그룹 영어이름")
    private String parentCelebNameEn;



}
