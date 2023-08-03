package com.sluv.server.domain.celeb.dto;

import com.sluv.server.domain.celeb.entity.Celeb;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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

    public static InterestedCelebChildResDto of(Celeb celeb){
        return InterestedCelebChildResDto.builder()
                .id(celeb.getId())
                .celebNameKr(celeb.getCelebNameKr())
                .build();
    }
}
