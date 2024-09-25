package com.sluv.api.celeb.dto.response;

import com.sluv.domain.celeb.entity.Celeb;
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
public class InterestedCelebParentResponse {
    @Schema(description = "상위 Celeb id")
    private Long id;
    @Schema(description = "Celeb 한글 이름")
    private String celebNameKr;
    @Schema(description = "Celeb 카테고리 이름")
    private String celebCategory;
    @Schema(description = "하위 Celeb 리스트")
    private List<InterestedCelebChildResponse> subCelebList;

    public static InterestedCelebParentResponse of(Celeb celeb) {
        List<InterestedCelebChildResponse> subDtoList = null;
        if (!celeb.getSubCelebList().isEmpty()) {
            subDtoList = celeb.getSubCelebList().stream()
                    .map(InterestedCelebChildResponse::of)
                    .toList();
        }
        return InterestedCelebParentResponse.builder()
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
