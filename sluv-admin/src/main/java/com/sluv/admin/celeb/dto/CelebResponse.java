package com.sluv.admin.celeb.dto;

import com.sluv.domain.celeb.entity.Celeb;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CelebResponse {

    private Long celebId;
    private Long parentId;
    private String parentName;
    private String krName;
    private String enName;
    private String parentCategory;
    private String category;

    public static CelebResponse from(Celeb celeb) {
        Long parentId = celeb.getParent() != null ? celeb.getParent().getId() : null;
        String parentName = celeb.getParent() != null ? celeb.getParent().getCelebNameKr() : null;
        String parentCategoryName = celeb.getCelebCategory().getParent() != null
                ? celeb.getCelebCategory().getParent().getName()
                : null;

        return CelebResponse.builder()
                .celebId(celeb.getId())
                .parentId(parentId)
                .parentName(parentName)
                .krName(celeb.getCelebNameKr())
                .enName(celeb.getCelebNameEn())
                .parentCategory(parentCategoryName)
                .category(celeb.getCelebCategory().getName())
                .build();
    }

}
