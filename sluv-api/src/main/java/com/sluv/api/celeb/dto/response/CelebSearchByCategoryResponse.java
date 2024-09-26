package com.sluv.api.celeb.dto.response;

import com.sluv.domain.celeb.entity.CelebCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CelebSearchByCategoryResponse {
    /**
     * 관심셀럽 선택 시 카테고리 그룹이 되는 Dto
     */
    @Schema(description = "카테고리 Id")
    private Long categoryId;
    @Schema(description = "카테고리 이름")
    private String categoryName;
    @Schema(description = "카테고리에 속한 셀럽 리스트")
    private List<CelebChipResponse> celebList;

    public static CelebSearchByCategoryResponse of(CelebCategory celebCategory, List<CelebChipResponse> list) {
        return CelebSearchByCategoryResponse.builder()
                .categoryId(celebCategory.getId())
                .categoryName(celebCategory.getName())
                .celebList(list)
                .build();
    }
}
