package com.sluv.api.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PaginationCountResponse<T> extends PaginationResponse<T> {
    @Schema(description = "전체 개수")
    private Long countNum;

    public PaginationCountResponse(Boolean hasNext, Integer page, List<T> content, Long countNum) {
        super(hasNext, page, content);
        this.countNum = countNum;
    }
}
