package com.sluv.domain.celeb.dto;

import com.sluv.domain.celeb.entity.Celeb;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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

    @Schema(description = "셀럽의 그룸 Id")
    private Long parentId;
    @Schema(description = "셀럽의 그룹 한글이름")
    private String parentCelebNameKr;
    @Schema(description = "셀럽의 그룹 영어이름")
    private String parentCelebNameEn;

    public static CelebDto of(Celeb celeb) {
        return CelebDto.builder()
                .id(celeb.getId())
                .celebNameKr(celeb.getCelebNameKr())
                .celebNameEn(celeb.getCelebNameEn())
                .categoryChild(celeb.getCelebCategory().getName())
                .categoryParent(
                        celeb.getCelebCategory().getParent() != null
                                ? celeb.getCelebCategory().getParent().getName()
                                : null)
                .parentId(celeb.getParent() != null ? celeb.getParent().getId() : null)
                .parentCelebNameKr(celeb.getParent() != null ? celeb.getParent().getCelebNameKr() : null)
                .parentCelebNameEn(celeb.getParent() != null ? celeb.getParent().getCelebNameEn() : null)
                .build();
    }


}
