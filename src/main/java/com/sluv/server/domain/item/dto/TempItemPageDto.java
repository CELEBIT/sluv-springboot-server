package com.sluv.server.domain.item.dto;

import com.sluv.server.global.common.response.PaginationResDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
public class TempItemPageDto<T> extends PaginationResDto<T> {
    @Schema(description = "임시저장 아이템의 개수")
    private Long count;

    public TempItemPageDto(Boolean hasNext, Integer page, List<T> content, Long count) {
        super(hasNext, page, content);
        this.count = count;
    }
}
