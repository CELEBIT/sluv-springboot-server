package com.sluv.server.domain.comment.dto;

import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.global.common.response.PaginationResDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@Getter
@SuperBuilder
public class SubCommentPageResDto<T> extends PaginationResDto<T> {
    private Long restCommentNum;

    public static SubCommentPageResDto<CommentResDto> of(Page<Comment> commentPage, List<CommentResDto> content, long restCommentNum){
        return SubCommentPageResDto.<CommentResDto>builder()
                .hasNext(commentPage.hasNext())
                .page(commentPage.getNumber())
                .content(content)
                .restCommentNum(restCommentNum)
                .build();
    }
}
