package com.sluv.server.domain.comment.repository.impl;

import static com.sluv.server.domain.comment.entity.QCommentLike.commentLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentLikeRepositoryImpl implements CommentLikeRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void withdrawByUserId(Long userId) {
        jpaQueryFactory.update(commentLike)
                .set(commentLike.user.id, -1L)
                .where(commentLike.user.id.eq(userId))
                .execute();
    }
}
