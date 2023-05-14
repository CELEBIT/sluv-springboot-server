package com.sluv.server.domain.celeb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecentSelectCelebResDto {
    @Schema(description = "child 셀럽의 Id")
    private Long id;
    @Schema(description = "parent 셀럽 Id")
    private Long parentId;
    @Schema(description = "child 셀럽의 이름")
    private String childCelebName;
    @Schema(description = "parent 셀럽의 이름")
    private String parentCelebName;
    @Schema(description = "셀럽(Y)과 뉴셀럽(N)을 구분하는 플래그")
    private String flag;

    @Builder
    public RecentSelectCelebResDto(Long id, Long parentId, String childCelebName, String parentCelebName, String flag) {
        this.id = id;
        this.parentId = parentId;
        this.childCelebName = childCelebName;
        this.parentCelebName = parentCelebName;
        this.flag = flag;
    }
}
