package com.sluv.server.domain.celeb.dto;

import com.sluv.server.domain.celeb.entity.Celeb;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestedCelebParentResDto {
    @Schema(description = "상위 Celeb id")
    private Long id;
    @Schema(description = "상위 Celeb 한글 이름")
    private String celebNameKr;
    @Schema(description = "하위 Celeb 리스트")
    private List<InterestedCelebChildResDto> subCelebList;

    public static InterestedCelebParentResDto of(Celeb celeb, List<InterestedCelebChildResDto> list){
        return InterestedCelebParentResDto.builder()
                .id(celeb.getId())
                .celebNameKr(celeb.getCelebNameKr())
                .subCelebList(list)
                .build();
    }

}
