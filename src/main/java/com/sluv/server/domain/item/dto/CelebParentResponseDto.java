package com.sluv.server.domain.item.dto;

import com.sluv.server.domain.celeb.entity.Celeb;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@NoArgsConstructor
public class CelebParentResponseDto {
    private Long id;
    private String celebNameKr;
    private String celebNameEn;

    private List<CelebResponseDto> subCelebList;

    @Builder
    public CelebParentResponseDto(Long id, String celebNameKr, String celebNameEn, List<CelebResponseDto> subCelebList) {
        this.id = id;
        this.celebNameKr = celebNameKr;
        this.celebNameEn = celebNameEn;
        this.subCelebList = subCelebList;
    }
}
