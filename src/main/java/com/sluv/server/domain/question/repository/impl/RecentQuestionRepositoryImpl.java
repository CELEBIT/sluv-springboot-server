package com.sluv.server.domain.question.repository.impl;

import static com.sluv.server.domain.question.entity.QRecentQuestion.recentQuestion;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.question.entity.RecentQuestion;
import com.sluv.server.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class RecentQuestionRepositoryImpl implements RecentQuestionRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<RecentQuestion> getUserAllRecentQuestion(User user, Pageable pageable) {
        List<RecentQuestion> content = jpaQueryFactory.selectFrom(recentQuestion)
                .where(recentQuestion.user.eq(user))
                .groupBy(recentQuestion.question)
                .orderBy(recentQuestion.createdAt.max().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<RecentQuestion> query = jpaQueryFactory.selectFrom(recentQuestion)
                .where(recentQuestion.user.eq(user))
                .groupBy(recentQuestion.question)
                .orderBy(recentQuestion.createdAt.max().desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }
}
