package com.sluv.server.domain.question.repository.impl;

import static com.sluv.server.domain.question.entity.QQuestionVote.questionVote;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuestionVoteRepositoryImpl implements QuestionVoteRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void withdrawByUserId(Long userId) {
        jpaQueryFactory.update(questionVote)
                .set(questionVote.user.id, -1L)
                .where(questionVote.user.id.eq(userId))
                .execute();
    }
}
