package com.sluv.server.domain.celeb.dto;

import com.sluv.server.domain.celeb.entity.InterestedCeleb;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestedCelebResDto {
    @Schema(description = "Celeb Id")
    private Long id;
    @Schema(description = "Celeb 한글 이름")
    private String celebNameKr;
    @Schema(description = "Celeb 카테고리 이름")
    private String celebCategory;

    public static InterestedCelebResDto of(InterestedCeleb interestedCeleb){
        return InterestedCelebResDto.builder()
                .id(interestedCeleb.getCeleb().getId())
                .celebNameKr(interestedCeleb.getCeleb().getCelebNameKr())
                .celebCategory(
                        interestedCeleb.getCeleb().getCelebCategory().getParent() != null
                        ? interestedCeleb.getCeleb().getCelebCategory().getParent().getName()
                        : interestedCeleb.getCeleb().getCelebCategory().getName()
                )
                .build();
    }
}
