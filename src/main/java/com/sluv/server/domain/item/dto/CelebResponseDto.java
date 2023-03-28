package com.sluv.server.domain.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class CelebResponseDto {
    private Long id;
    private String celebNameKr;
    private String celebNameEn;

    @Builder
    public CelebResponseDto(Long id, String celebNameKr, String celebNameEn) {
        this.id = id;
        this.celebNameKr = celebNameKr;
        this.celebNameEn = celebNameEn;
    }
}
