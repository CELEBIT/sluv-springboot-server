package com.sluv.api.celeb.dto.response;

import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.NewCeleb;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CelebChipResponse {
    /**
     * 관심셀럽 선택 시 카테고리 안에 들어가는 셀럽 Dto
     */
    @Schema(description = "셀럽 Id")
    private Long celebId;
    @Schema(description = "셀럽 이름")
    private String celebName;

    public static CelebChipResponse of(Celeb celeb) {

        String celebName = celeb.getParent() != null
                ? celeb.getParent().getCelebNameKr() + " " + celeb.getCelebNameKr()
                : celeb.getCelebNameKr();

        return CelebChipResponse.builder()
                .celebId(celeb.getId())
                .celebName(celebName)
                .build();
    }

    public static CelebChipResponse of(NewCeleb celeb) {
        return CelebChipResponse.builder()
                .celebId(celeb.getId())
                .celebName(celeb.getCelebName())
                .build();
    }

}
