package com.sluv.server.domain.question.repository.impl;

import static com.sluv.server.domain.question.entity.QQuestionLike.questionLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuestionLikeRepositoryImpl implements QuestionLikeRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void withdrawByUserId(Long userId) {
        jpaQueryFactory.update(questionLike)
                .set(questionLike.user.id, -1L)
                .where(questionLike.user.id.eq(userId))
                .execute();
    }
}
