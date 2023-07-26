package com.sluv.server.domain.comment.dto;

import com.sluv.server.global.common.response.PaginationResDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class SubCommentPageResDto<T> extends PaginationResDto<T> {
    private Long restCommentNum;

    public SubCommentPageResDto(Boolean hasNext, Integer page, List<T> content, Long restCommentNum) {
        super(hasNext, page, content);
        this.restCommentNum = restCommentNum;
    }
}
