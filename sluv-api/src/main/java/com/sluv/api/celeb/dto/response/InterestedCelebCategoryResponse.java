package com.sluv.api.celeb.dto.response;

import com.sluv.domain.celeb.entity.CelebCategory;
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
public class InterestedCelebCategoryResponse {
    @Schema(description = "카테고리 이름")
    private String categoryName;
    @Schema(description = "카테고리에 속한 셀럽 리스트")
    private List<InterestedCelebParentResponse> celebList;

    public static InterestedCelebCategoryResponse of(CelebCategory celebCategory,
                                                     List<InterestedCelebParentResponse> list) {
        return InterestedCelebCategoryResponse.builder()
                .categoryName(celebCategory.getName())
                .celebList(list)
                .build();
    }

}
