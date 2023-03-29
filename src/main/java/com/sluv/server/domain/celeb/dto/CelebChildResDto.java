package com.sluv.server.domain.celeb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class CelebChildResDto {
    @Schema(description = "멤버 Celeb Id")
    private Long id;
    @Schema(description = "멤버 Celeb 한글 이름")
    private String celebNameKr;
    @Schema(description = "멤버 Celeb 영어 이름")
    private String celebNameEn;

    @Builder
    public CelebChildResDto(Long id, String celebNameKr, String celebNameEn) {
        this.id = id;
        this.celebNameKr = celebNameKr;
        this.celebNameEn = celebNameEn;
    }
}
