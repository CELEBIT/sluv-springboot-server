package com.sluv.server.domain.celeb.dto;

import com.sluv.server.domain.celeb.entity.Celeb;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestedCelebChildResDto {
    @Schema(description = "멤버 Celeb Id")
    private Long id;
    @Schema(description = "멤버 Celeb 한글 이름")
    private String celebNameKr;
    @Schema(description = "Celeb 카테고리 이름")
    private String celebCategory;

    public static InterestedCelebChildResDto of(Celeb celeb) {
        return InterestedCelebChildResDto.builder()
                .id(celeb.getId())
                .celebNameKr(celeb.getCelebNameKr())
                .celebCategory(
                        celeb.getCelebCategory().getParent() != null
                                ? celeb.getCelebCategory().getParent().getName()
                                : celeb.getCelebCategory().getName()
                )
                .build();
    }
}
