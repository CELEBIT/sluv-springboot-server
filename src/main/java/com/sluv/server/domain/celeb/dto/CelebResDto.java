package com.sluv.server.domain.celeb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CelebResDto {
    @Schema(description = "셀럽의 Id")
    private Long celebIdx;
    @Schema(description = "셀럽의 이름")
    private String name;
    @Schema(description = "셀럽이 그룹일때, 멤버들 리스트.")
    private List<MemberResDto> memberList;

    @Builder
    public CelebResDto(Long celebIdx, String name, List<MemberResDto> memberList) {
        this.celebIdx = celebIdx;
        this.name = name;
        this.memberList = memberList;
    }
}
