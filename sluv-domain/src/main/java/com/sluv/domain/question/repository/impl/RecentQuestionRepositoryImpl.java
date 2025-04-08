package com.sluv.domain.question.repository.impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.question.entity.QQuestion;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.enums.QuestionStatus;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sluv.domain.question.entity.QRecentQuestion.recentQuestion;

@RequiredArgsConstructor
public class RecentQuestionRepositoryImpl implements RecentQuestionRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Question> getUserAllRecentQuestion(User user, List<Long> blockUserIds, Pageable pageable) {
        QQuestion question = new QQuestion("question");
        List<Question> content = jpaQueryFactory.select(question)
                .from(question)
                .leftJoin(recentQuestion).on(recentQuestion.question.eq(question))
                .where(recentQuestion.user.eq(user)
                        .and(recentQuestion.question.questionStatus.eq(QuestionStatus.ACTIVE))
                        .and(recentQuestion.question.user.id.notIn(blockUserIds))
                )
                .groupBy(recentQuestion.question)
                .orderBy(recentQuestion.createdAt.max().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<Question> query = jpaQueryFactory.select(question)
                .from(question)
                .leftJoin(recentQuestion).on(recentQuestion.question.eq(question))
                .where(recentQuestion.user.eq(user)
                        .and(recentQuestion.question.questionStatus.eq(QuestionStatus.ACTIVE))
                        .and(recentQuestion.question.user.id.notIn(blockUserIds))
                )
                .groupBy(recentQuestion.question)
                .orderBy(recentQuestion.createdAt.max().desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }
}
