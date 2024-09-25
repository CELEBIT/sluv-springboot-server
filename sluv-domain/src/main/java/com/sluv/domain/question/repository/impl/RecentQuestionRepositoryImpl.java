package com.sluv.domain.question.repository.impl;

import static com.sluv.domain.question.entity.QRecentQuestion.recentQuestion;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.question.entity.QQuestion;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.enums.QuestionStatus;
import com.sluv.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class RecentQuestionRepositoryImpl implements RecentQuestionRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Question> getUserAllRecentQuestion(User user, Pageable pageable) {
        QQuestion question = new QQuestion("question");
        List<Question> content = jpaQueryFactory.select(question)
                .from(question)
                .leftJoin(recentQuestion).on(recentQuestion.question.eq(question)).fetchJoin()
                .where(recentQuestion.user.eq(user)
                        .and(recentQuestion.question.questionStatus.eq(QuestionStatus.ACTIVE)))
                .groupBy(recentQuestion.question)
                .orderBy(recentQuestion.createdAt.max().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<Question> query = jpaQueryFactory.select(question)
                .from(question)
                .leftJoin(recentQuestion).on(recentQuestion.question.eq(question)).fetchJoin()
                .where(recentQuestion.user.eq(user)
                        .and(recentQuestion.question.questionStatus.eq(QuestionStatus.ACTIVE)))
                .groupBy(recentQuestion.question)
                .orderBy(recentQuestion.createdAt.max().desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }
}
