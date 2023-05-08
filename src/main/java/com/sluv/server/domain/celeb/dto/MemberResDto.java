package com.sluv.server.domain.celeb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberResDto {
    @Schema(description = "셀럽의 Id")
    private Long memberIdx;
    @Schema(description = "셀럽의 이름")
    private String name;

    @Builder
    public MemberResDto(Long memberIdx, String name) {
        this.memberIdx = memberIdx;
        this.name = name;
    }
}
