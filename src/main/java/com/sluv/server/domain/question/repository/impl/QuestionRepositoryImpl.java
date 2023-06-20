package com.sluv.server.domain.question.repository.impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.question.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sluv.server.domain.question.entity.QQuestionBuy.questionBuy;
import static com.sluv.server.domain.question.entity.QQuestionFind.questionFind;
import static com.sluv.server.domain.question.entity.QQuestionHowabout.questionHowabout;
import static com.sluv.server.domain.question.entity.QQuestionRecommend.questionRecommend;
import static com.sluv.server.domain.question.enums.QuestionStatus.ACTIVE;

@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<QuestionBuy> getSearchQuestionBuy(List<Long> questionIdList, Pageable pageable) {
        List<QuestionBuy> content = jpaQueryFactory.selectFrom(questionBuy)
                .where(questionBuy.id.in(questionIdList)
                        .and(questionBuy.questionStatus.eq(ACTIVE))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(questionBuy.createdAt.desc()) // 추구 후현
                .fetch();

        //Count Query
        JPAQuery<QuestionBuy> countJPAQuery = jpaQueryFactory.selectFrom(questionBuy)
                .where(questionBuy.id.in(questionIdList)
                        .and(questionBuy.questionStatus.eq(ACTIVE))
                )
                .orderBy(questionBuy.createdAt.desc());// 추구 후현


        return PageableExecutionUtils.getPage(content, pageable, () -> countJPAQuery.fetch().size());
    }

    @Override
    public Page<QuestionFind> getSearchQuestionFind(List<Long> questionIdList, Pageable pageable) {
        List<QuestionFind> content = jpaQueryFactory.selectFrom(questionFind)
                .where(questionFind.id.in(questionIdList)
                        .and(questionFind.questionStatus.eq(ACTIVE))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(questionFind.createdAt.desc()) // 추구 후현
                .fetch();

        //Count Query
        JPAQuery<QuestionFind> countJPAQuery = jpaQueryFactory.selectFrom(questionFind)
                .where(questionFind.id.in(questionIdList)
                        .and(questionFind.questionStatus.eq(ACTIVE))
                )
                .orderBy(questionFind.createdAt.desc());// 추구 후현


        return PageableExecutionUtils.getPage(content, pageable, () -> countJPAQuery.fetch().size());
    }

    @Override
    public Page<QuestionHowabout> getSearchQuestionHowabout(List<Long> questionIdList, Pageable pageable) {
        List<QuestionHowabout> content = jpaQueryFactory.selectFrom(questionHowabout)
                .where(questionHowabout.id.in(questionIdList)
                        .and(questionHowabout.questionStatus.eq(ACTIVE))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(questionHowabout.createdAt.desc()) // 추구 후현
                .fetch();

        //Count Query
        JPAQuery<QuestionHowabout> countJPAQuery = jpaQueryFactory.selectFrom(questionHowabout)
                .where(questionHowabout.id.in(questionIdList)
                        .and(questionHowabout.questionStatus.eq(ACTIVE))
                )
                .orderBy(questionHowabout.createdAt.desc());// 추구 후현


        return PageableExecutionUtils.getPage(content, pageable, () -> countJPAQuery.fetch().size());
    }

    @Override
    public Page<QuestionRecommend> getSearchQuestionRecommend(List<Long> questionIdList, Pageable pageable) {
        List<QuestionRecommend> content = jpaQueryFactory.selectFrom(questionRecommend)
                .where(questionRecommend.id.in(questionIdList)
                        .and(questionRecommend.questionStatus.eq(ACTIVE))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(questionRecommend.createdAt.desc()) // 추구 후현
                .fetch();

        //Count Query
        JPAQuery<QuestionRecommend> countJPAQuery = jpaQueryFactory.selectFrom(questionRecommend)
                .where(questionRecommend.id.in(questionIdList)
                        .and(questionRecommend.questionStatus.eq(ACTIVE))
                )
                .orderBy(questionRecommend.createdAt.desc());// 추구 후현


        return PageableExecutionUtils.getPage(content, pageable, () -> countJPAQuery.fetch().size());
    }
}