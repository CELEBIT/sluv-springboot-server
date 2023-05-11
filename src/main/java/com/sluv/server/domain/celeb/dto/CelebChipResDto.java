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
public class CelebChipResDto {
    /**
     * 관심셀럽 선택 시 카테고리 안에 들어가는 셀럽 Dto
     */
    @Schema(description = "셀럽 Id")
    private Long celebId;
    @Schema(description = "셀럽 이름")
    private String celebName;

}