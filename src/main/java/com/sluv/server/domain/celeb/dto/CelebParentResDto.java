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
public class CelebParentResDto {
    @Schema(description = "상위 Celeb id")
    private Long id;
    @Schema(description = "상위 Celeb 한글 이름")
    private String celebNameKr;
    @Schema(description = "상위 Celeb 영어 이름")
    private String celebNameEn;
    @Schema(description = "하위 Celeb 리스트")
    private List<CelebChildResDto> subCelebList;

    @Builder
    public CelebParentResDto(Long id, String celebNameKr, String celebNameEn, List<CelebChildResDto> subCelebList) {
        this.id = id;
        this.celebNameKr = celebNameKr;
        this.celebNameEn = celebNameEn;
        this.subCelebList = subCelebList;
    }
}
