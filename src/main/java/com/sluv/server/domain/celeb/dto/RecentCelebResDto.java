package com.sluv.server.domain.celeb.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecentCelebResDto {
    private Long id;
    private String celebName;
    private String flag;

    @Builder
    public RecentCelebResDto(Long id, String celebName, String flag) {
        this.id = id;
        this.celebName = celebName;
        this.flag = flag;
    }
}
