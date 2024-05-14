package com.sluv.server.domain.celeb.dto;

import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.NewCeleb;
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

    public static CelebChipResDto of(Celeb celeb) {
        return CelebChipResDto.builder()
                .celebId(celeb.getId())
                .celebName(celeb.getCelebNameKr())
                .build();
    }

    public static CelebChipResDto of(NewCeleb celeb) {
        return CelebChipResDto.builder()
                .celebId(celeb.getId())
                .celebName(celeb.getCelebName())
                .build();
    }

}
