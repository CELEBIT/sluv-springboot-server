package com.sluv.server.domain.celeb.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@JsonPropertyOrder({"id", "CelebNameKr", "CelebNameEn"})
public class CelebSearchResDto {
    @Schema(description = "Celeb id")
    private Long id;
    @Schema(description = "Celeb 한글 이름")
    private String CelebNameKr;
    @Schema(description = "Celeb 영어 이름")
    private String CelebNameEn;

    @Builder
    public CelebSearchResDto(Long id, String celebNameKr, String celebNameEn) {
        this.id = id;
        CelebNameKr = celebNameKr;
        CelebNameEn = celebNameEn;
    }
}
