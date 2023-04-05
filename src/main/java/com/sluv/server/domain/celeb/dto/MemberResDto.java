package com.sluv.server.domain.celeb.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberResDto {
    private Long memberIdx;
    private String name;

    @Builder
    public MemberResDto(Long memberIdx, String name) {
        this.memberIdx = memberIdx;
        this.name = name;
    }
}
