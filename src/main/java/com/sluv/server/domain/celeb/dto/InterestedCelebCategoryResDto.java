package com.sluv.server.domain.celeb.dto;

import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.CelebCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestedCelebCategoryResDto {
    @Schema(description = "카테고리 이름")
    private String categoryName;
    @Schema(description = "카테고리에 속한 셀럽 리스트")
    private List<InterestedCelebParentResDto> celebList;

    public static InterestedCelebCategoryResDto of(CelebCategory celebCategory, List<InterestedCelebParentResDto> list){
        return InterestedCelebCategoryResDto.builder()
                .categoryName(celebCategory.getName())
                .celebList(list)
                .build();
    }

}
