package com.sluv.server.domain.comment.repository.impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.domain.comment.enums.CommentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sluv.server.domain.comment.entity.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<Comment> getAllQuestionComment(Long questionId, Pageable pageable) {
        List<Comment> content = jpaQueryFactory.selectFrom(comment)
                .where(comment.question.id.eq(questionId)
                        .and(comment.parent.isNull())
                        .and(comment.commentStatus.eq(CommentStatus.ACTIVE))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count Query
        JPAQuery<Comment> countQuery = jpaQueryFactory.selectFrom(comment)
                .where(comment.question.id.eq(questionId));


        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    @Override
    public Page<Comment> getAllSubComment(Long commentId, Pageable pageable) {
        List<Comment> content = jpaQueryFactory.selectFrom(comment)
                .where(comment.parent.id.eq(commentId)
                        .and(comment.commentStatus.eq(CommentStatus.ACTIVE))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count Query
        JPAQuery<Comment> countQuery = jpaQueryFactory.selectFrom(comment)
                .where(comment.parent.id.eq(commentId));


        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }
}
