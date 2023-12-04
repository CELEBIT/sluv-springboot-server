package com.sluv.server.global.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PaginationResDto<T> {
    @Schema(description = "다음 페이지 존재 여부")
    private Boolean hasNext;
    @Schema(description = "현재 page")
    private Integer page;
    @Schema(description = "데이터들")
    private List<T> content;

    public static <T> PaginationResDto<T> of(Page<?> page, List<T> content) {
        return PaginationResDto.<T>builder()
                .page(page.getNumber())
                .hasNext(page.hasNext())
                .content(content)
                .build();
    }
}
