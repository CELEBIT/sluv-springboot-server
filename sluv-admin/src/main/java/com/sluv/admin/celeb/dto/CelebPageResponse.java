package com.sluv.admin.celeb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CelebPageResponse {

    private Integer pageNumber;
    private Integer totalPageSize;
    private List<CelebResponse> content;

    public static CelebPageResponse of(Integer pageNumber, Integer totalPageSize, List<CelebResponse> content) {
        return CelebPageResponse.builder()
                .pageNumber(pageNumber + 1)
                .totalPageSize(totalPageSize)
                .content(content)
                .build();
    }

}
