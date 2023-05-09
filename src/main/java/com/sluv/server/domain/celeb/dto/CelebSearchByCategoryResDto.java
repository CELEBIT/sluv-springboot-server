package com.sluv.server.domain.celeb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CelebSearchByCategoryResDto {
    /**
     * 관심셀럽 선택 시 카테고리 그룹이 되는 Dto
     */
    @Schema(description = "카테고리 Id")
    private Long categoryId;
    @Schema(description = "카테고리 이름")
    private String categoryName;
    @Schema(description = "카테고리에 속한 셀럽 리스트")
    private List<CelebChipResDto> celebList;

}
