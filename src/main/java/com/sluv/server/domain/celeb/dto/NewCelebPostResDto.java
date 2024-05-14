package com.sluv.server.domain.celeb.dto;

import com.sluv.server.domain.celeb.entity.NewCeleb;
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
    @Schema(description = "생성된 newCeleb의 Id")
    private Long newCelebId;
    @Schema(description = "생성된 newCeleb의 이름")
    private String newCelebName;

    public static NewCelebPostResDto of(NewCeleb newCeleb) {
        return NewCelebPostResDto.builder()
                .newCelebId(newCeleb.getId())
                .newCelebName(newCeleb.getCelebName())
                .build();
    }
}
