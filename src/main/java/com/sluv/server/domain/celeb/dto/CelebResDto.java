package com.sluv.server.domain.celeb.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CelebResDto {
    private Long celebIdx;
    private String name;
    private List<MemberResDto> memberList;

    @Builder
    public CelebResDto(Long celebIdx, String name, List<MemberResDto> memberList) {
        this.celebIdx = celebIdx;
        this.name = name;
        this.memberList = memberList;
    }
}
