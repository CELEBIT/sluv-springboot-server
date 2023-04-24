package com.sluv.server.domain.celeb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class InterestedCelebChildResDto {
    @Schema(description = "멤버 Celeb Id")
    private Long id;
    @Schema(description = "멤버 Celeb 한글 이름")
    private String celebNameKr;

    @Builder
    public InterestedCelebChildResDto(Long id, String celebNameKr) {
        this.id = id;
        this.celebNameKr = celebNameKr;
    }
}
