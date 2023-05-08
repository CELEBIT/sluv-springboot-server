package com.sluv.server.domain.celeb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewCelebPostReqDto {
    @Schema(description = "뉴셀럽의 이름")
    private String newCelebName;
}
