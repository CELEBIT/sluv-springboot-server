package com.sluv.server.domain.comment.repository.impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.domain.comment.enums.CommentStatus;
import com.sluv.server.domain.question.enums.QuestionStatus;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sluv.server.domain.comment.entity.QComment.comment;
import static com.sluv.server.domain.comment.entity.QCommentLike.commentLike;

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
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count Query
        JPAQuery<Comment> countQuery = jpaQueryFactory.selectFrom(comment)
                .where(comment.question.id.eq(questionId)
                        .and(comment.parent.isNull())
                        .and(comment.commentStatus.eq(CommentStatus.ACTIVE))
                )
                .orderBy(comment.createdAt.desc());


        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    @Override
    public Page<Comment> getAllSubComment(Long commentId, Pageable pageable) {
        List<Comment> content = jpaQueryFactory.selectFrom(comment)
                .where(comment.parent.id.eq(commentId)
                        .and(comment.commentStatus.eq(CommentStatus.ACTIVE))
                )
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count Query
        JPAQuery<Comment> countQuery = jpaQueryFactory.selectFrom(comment)
                .where(comment.parent.id.eq(commentId)
                        .and(comment.commentStatus.eq(CommentStatus.ACTIVE))
                )
                .orderBy(comment.createdAt.desc());


        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    @Override
    public Page<Comment> getUserAllLikeComment(User user, Pageable pageable) {
        List<Comment> content = jpaQueryFactory.select(comment)
                .from(commentLike)
                .where(commentLike.user.eq(user)
                        .and(comment.commentStatus.eq(CommentStatus.ACTIVE))
                        .and(comment.question.questionStatus.eq(QuestionStatus.ACTIVE))
                )
                .orderBy(commentLike.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // CountQuery
        JPAQuery<Comment> query = jpaQueryFactory.select(comment)
                .from(commentLike)
                .where(commentLike.user.eq(user)
                        .and(comment.commentStatus.eq(CommentStatus.ACTIVE))
                        .and(comment.question.questionStatus.eq(QuestionStatus.ACTIVE))
                )
                .orderBy(commentLike.createdAt.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }

    /**
     * 현재 유저가 업로드한 Comment 조회
     */
    @Override
    public Page<Comment> getUserAllComment(User user, Pageable pageable) {
        List<Comment> content = jpaQueryFactory.selectFrom(comment)
                .where(comment.user.eq(user)
                        .and(comment.commentStatus.eq(CommentStatus.ACTIVE))
                        .and(comment.question.questionStatus.eq(QuestionStatus.ACTIVE))
                )
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<Comment> query = jpaQueryFactory.selectFrom(comment)
                .where(comment.user.eq(user)
                        .and(comment.commentStatus.eq(CommentStatus.ACTIVE))
                        .and(comment.question.questionStatus.eq(QuestionStatus.ACTIVE))
                )
                .orderBy(comment.createdAt.desc());


        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }
}
