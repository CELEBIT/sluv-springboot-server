package com.sluv.server.domain.celeb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCelebPostResDto {
    @Schema(description = "NewCeleb의 Id")
    private Long newCelebId;
    @Schema(description = "NewCeleb의 이름")
    private String newCelebName;
}
