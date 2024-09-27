package com.sluv.api.celeb.dto.response;

import com.sluv.domain.celeb.entity.Celeb;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestedCelebResponse {
    @Schema(description = "Celeb Id")
    private Long id;
    @Schema(description = "Celeb 한글 이름")
    private String celebNameKr;
    @Schema(description = "Celeb 카테고리 이름")
    private String celebCategory;

    public static InterestedCelebResponse of(Celeb celeb) {
        return InterestedCelebResponse.builder()
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
