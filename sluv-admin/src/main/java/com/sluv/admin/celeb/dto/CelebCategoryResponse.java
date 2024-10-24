package com.sluv.admin.celeb.dto;

import com.sluv.domain.celeb.entity.CelebCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CelebCategoryResponse {

    private Long celebCategoryId;
    private String celebCategoryName;

    public static CelebCategoryResponse from(CelebCategory celebCategory) {
        String celebCategoryName = celebCategory.getParent().getName() + " -> " + celebCategory.getName();
        return CelebCategoryResponse.builder()
                .celebCategoryId(celebCategory.getId())
                .celebCategoryName(celebCategoryName)
                .build();
    }
}
