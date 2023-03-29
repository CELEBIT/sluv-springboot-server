package com.sluv.server.domain.celeb.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@JsonPropertyOrder({"id", "CelebNameKr", "CelebNameEn"})
public class CelebSearchResDto {
    private Long id;
    private String CelebNameKr;
    private String CelebNameEn;

    @Builder
    public CelebSearchResDto(Long id, String celebNameKr, String celebNameEn) {
        this.id = id;
        CelebNameKr = celebNameKr;
        CelebNameEn = celebNameEn;
    }
}
