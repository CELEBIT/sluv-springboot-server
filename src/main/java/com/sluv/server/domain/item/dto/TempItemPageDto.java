package com.sluv.server.domain.item.dto;

import com.sluv.server.global.common.response.PaginationResDto;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
public class TempItemPageDto<T> extends PaginationResDto<T> {
    private Long count;

    public TempItemPageDto(Boolean hasNext, Integer page, List<T> content, Long count) {
        super(hasNext, page, content);
        this.count = count;
    }
}
