package com.sluv.server.domain.celeb.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecentSelectCelebResDto {
    private Long id;
    private String celebName;
    private String flag;

    @Builder
    public RecentSelectCelebResDto(Long id, String celebName, String flag) {
        this.id = id;
        this.celebName = celebName;
        this.flag = flag;
    }
}
