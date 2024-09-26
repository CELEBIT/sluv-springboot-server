package com.sluv.api.celeb.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewCelebPostRequest {
    @Schema(description = "뉴셀럽의 이름")
    private String newCelebName;
}
