package com.sluv.api.celeb.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestedCelebPostRequest {
    @Schema(description = "유저가 선택한 관심셀럽의 Id 리스트")
    private List<Long> celebIdList;

    @Schema(description = "유저가 선택한 뉴관심셀럽의 이름 리스트")
    private List<String> celebNameList;
}
