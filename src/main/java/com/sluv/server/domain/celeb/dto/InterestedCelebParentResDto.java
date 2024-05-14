package com.sluv.server.domain.celeb.dto;

import com.sluv.server.domain.celeb.entity.Celeb;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
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
public class InterestedCelebParentResDto {
    @Schema(description = "상위 Celeb id")
    private Long id;
    @Schema(description = "Celeb 한글 이름")
    private String celebNameKr;
    @Schema(description = "Celeb 카테고리 이름")
    private String celebCategory;
    @Schema(description = "하위 Celeb 리스트")
    private List<InterestedCelebChildResDto> subCelebList;

    public static InterestedCelebParentResDto of(Celeb celeb) {
        List<InterestedCelebChildResDto> subDtoList = null;
        if (!celeb.getSubCelebList().isEmpty()) {
            subDtoList = celeb.getSubCelebList().stream()
                    .map(InterestedCelebChildResDto::of)
                    .toList();
        }
        return InterestedCelebParentResDto.builder()
                .id(celeb.getId())
                .celebNameKr(celeb.getCelebNameKr())
                .celebCategory(
                        celeb.getCelebCategory().getParent() != null
                                ? celeb.getCelebCategory().getParent().getName()
                                : celeb.getCelebCategory().getName()
                )
                .subCelebList(subDtoList)
                .build();
    }

}
