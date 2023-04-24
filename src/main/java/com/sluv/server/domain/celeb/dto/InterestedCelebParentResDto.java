package com.sluv.server.domain.celeb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
public class InterestedCelebParentResDto {
    @Schema(description = "상위 Celeb id")
    private Long id;
    @Schema(description = "상위 Celeb 한글 이름")
    private String celebNameKr;
    @Schema(description = "하위 Celeb 리스트")
    private List<InterestedCelebChildResDto> subCelebList;

    @Builder
    public InterestedCelebParentResDto(Long id, String celebNameKr, List<InterestedCelebChildResDto> subCelebList) {
        this.id = id;
        this.celebNameKr = celebNameKr;
        this.subCelebList = subCelebList;
    }
}
