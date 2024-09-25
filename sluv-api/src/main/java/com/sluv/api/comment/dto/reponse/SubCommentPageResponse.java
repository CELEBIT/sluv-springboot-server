package com.sluv.api.comment.dto.reponse;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.domain.comment.entity.Comment;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

@NoArgsConstructor
@Getter
@SuperBuilder
public class SubCommentPageResponse<T> extends PaginationResponse<T> {
    private Long restCommentNum;

    public static SubCommentPageResponse<CommentResponse> of(Page<Comment> commentPage, List<CommentResponse> content,
                                                             long restCommentNum) {
        return SubCommentPageResponse.<CommentResponse>builder()
                .hasNext(commentPage.hasNext())
                .page(commentPage.getNumber())
                .content(content)
                .restCommentNum(restCommentNum)
                .build();
    }
}
