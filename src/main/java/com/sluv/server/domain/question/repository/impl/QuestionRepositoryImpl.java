package com.sluv.server.domain.question.repository.impl;

import static com.sluv.server.domain.celeb.entity.QCeleb.celeb;
import static com.sluv.server.domain.celeb.entity.QNewCeleb.newCeleb;
import static com.sluv.server.domain.comment.entity.QComment.comment;
import static com.sluv.server.domain.question.entity.QDailyHotQuestion.dailyHotQuestion;
import static com.sluv.server.domain.question.entity.QQuestion.question;
import static com.sluv.server.domain.question.entity.QQuestionBuy.questionBuy;
import static com.sluv.server.domain.question.entity.QQuestionFind.questionFind;
import static com.sluv.server.domain.question.entity.QQuestionHowabout.questionHowabout;
import static com.sluv.server.domain.question.entity.QQuestionLike.questionLike;
import static com.sluv.server.domain.question.entity.QQuestionRecommend.questionRecommend;
import static com.sluv.server.domain.question.entity.QQuestionRecommendCategory.questionRecommendCategory;
import static com.sluv.server.domain.question.enums.QuestionStatus.ACTIVE;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.QCeleb;
import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.question.entity.QuestionBuy;
import com.sluv.server.domain.question.entity.QuestionFind;
import com.sluv.server.domain.question.entity.QuestionHowabout;
import com.sluv.server.domain.question.entity.QuestionRecommend;
import com.sluv.server.domain.user.entity.User;
import com.ulisesbocchio.jasyptspringboot.util.Collections;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepositoryCustom {
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

    /**
     * Wait QuestionBuy 조회
     */
    @Override
    public List<QuestionBuy> getWaitQuestionBuy(User user, Long questionId, List<Celeb> interestedCeleb) {
        LocalDateTime nowTime = LocalDateTime.now();
        return jpaQueryFactory.selectFrom(questionBuy)
                .where(questionBuy.id.ne(questionId)
                        .and(questionBuy.questionStatus.eq(ACTIVE))
                        .and(questionBuy.voteEndTime.gt(nowTime))
                        .and(getUserNeOrNullInQuestion(questionBuy, user))
                )
                .orderBy(questionBuy.voteEndTime.asc())
                .limit(4)
                .fetch();
    }

    /**
     * Wait QuestionRecommend 조회
     */
    @Override
    public List<QuestionRecommend> getWaitQuestionRecommend(User user, Long questionId) {
        return jpaQueryFactory.select(questionRecommend)
                .from(questionRecommend)
                .where(questionRecommend.id.ne(questionId)
                        .and(questionRecommend.questionStatus.eq(ACTIVE))
                        .and(getUserNeOrNullInQuestion(questionRecommend, user))
                        .and(questionRecommend.commentList.size().eq(0))
                )
                .orderBy(questionRecommend.createdAt.desc())
                .limit(4)
                .fetch();
    }

    /**
     * Wait QuestionHowabout 조회
     */
    @Override
    public List<QuestionHowabout> getWaitQuestionHowabout(User user, Long questionId) {
        return jpaQueryFactory.select(questionHowabout)
                .from(questionHowabout)
                .where(questionHowabout.id.ne(questionId)
                        .and(questionHowabout.questionStatus.eq(ACTIVE))
                        .and(getUserNeOrNullInQuestion(questionHowabout, user))
                        .and(questionHowabout.commentList.size().eq(0))
                )
                .orderBy(questionHowabout.createdAt.desc())
                .limit(4)
                .fetch();
    }

    /**
     * Wait QuestionFind 조회
     * TODO: 게시글과 같은 셀럽/같은 그룹 로직 추가
     */
    @Override
    public List<QuestionFind> getWaitQuestionFind(User user, Long questionId, List<Celeb> interestedCeleb) {

        return jpaQueryFactory.select(questionFind)
                .from(questionFind)
                .where(questionFind.id.ne(questionId)
                        .and(questionFind.questionStatus.eq(ACTIVE))
                        .and(getUserNeOrNullInQuestion(questionFind, user))
                        .and(questionFind.commentList.size().eq(0))
                )
                .orderBy(questionFind.createdAt.desc())
                .limit(4)
                .fetch();
    }

    private static <T> BooleanBuilder getUserNeOrNullInQuestion(EntityPathBase<T> entity, User user) {
        BooleanBuilder whereClause = new BooleanBuilder();
        if (user != null) {
            whereClause.and(Expressions.path(Long.class, entity, "user.id").ne(user.getId()));
        }
        return whereClause;
    }

    /**
     * User Like Question
     */
    @Override
    public Page<Question> getUserLikeQuestion(User user, Pageable pageable) {
        List<Question> content = jpaQueryFactory.select(question)
                .from(questionLike)
                .leftJoin(questionLike.question, question)
                .where(questionLike.user.eq(user)
                        .and(question.questionStatus.eq(ACTIVE))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(questionLike.createdAt.desc())
                .fetch();
        //Count Query
        JPAQuery<Question> query = jpaQueryFactory.select(question)
                .from(questionLike)
                .leftJoin(questionLike.question, question)
                .where(questionLike.user.eq(user)
                        .and(question.questionStatus.eq(ACTIVE))
                )
                .orderBy(questionLike.createdAt.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }

    /**
     * 현재 유저가 작성한 모든 Question 조회
     */
    @Override
    public Page<Question> getUserAllQuestion(User user, Pageable pageable) {
        List<Question> content = jpaQueryFactory.selectFrom(question)
                .where(question.user.eq(user)
                        .and(question.questionStatus.eq(ACTIVE))
                )
                .orderBy(question.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<Question> query = jpaQueryFactory.selectFrom(question)
                .where(question.user.eq(user)
                        .and(question.questionStatus.eq(ACTIVE))
                )
                .orderBy(question.createdAt.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }

    @Override
    public Page<Question> getTotalQuestionList(Pageable pageable) {
        List<Question> content = jpaQueryFactory.selectFrom(question)
                .where(question.questionStatus.eq(ACTIVE))
                .orderBy(question.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<Question> query = jpaQueryFactory.selectFrom(question)
                .where(question.questionStatus.eq(ACTIVE))
                .orderBy(question.createdAt.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }

    /**
     * QuestionBuy만 조회. Filtering: voteStatus =Ordering= 전체 → 최신순 진행 중 → 최신순 종료 임박 → 종료 임박 순 종료 → 최신순
     * <p>
     * voteStatus가 null이면 모든 voteStatus에 대해 조회.
     */
    @Override
    public Page<QuestionBuy> getQuestionBuyList(String voteStatus, Pageable pageable) {

        List<QuestionBuy> content = jpaQueryFactory.selectFrom(questionBuy)
                .where(getQuestionBuyFiltering(voteStatus))
                .orderBy(getQuestionBuyOrderSpecifier(voteStatus))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<QuestionBuy> query = jpaQueryFactory.selectFrom(questionBuy)
                .where(getQuestionBuyFiltering(voteStatus));
//                    .orderBy(getQuestionBuyOrderSpecifier());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }

    /**
     * QuestionFind만 조회. Ordering: createdAt Filtering: CelebId
     * <p>
     * CelebId가 null이면 모든 Celeb에 대해 조회.
     */
    @Override
    public Page<QuestionFind> getQuestionFindList(Long celebId, Pageable pageable) {

        List<QuestionFind> content = jpaQueryFactory.selectFrom(questionFind)
                .where(getQuestionFindFiltering(celebId))
                .orderBy(questionFind.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<QuestionFind> query = jpaQueryFactory.selectFrom(questionFind)
                .where(getQuestionFindFiltering(celebId))
                .orderBy(questionFind.createdAt.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }

    /**
     * QuestionHowabout만 조회. Ordering: createdAt
     */
    @Override
    public Page<QuestionHowabout> getQuestionHowaboutList(Pageable pageable) {
        List<QuestionHowabout> content = jpaQueryFactory.selectFrom(questionHowabout)
                .where(questionHowabout.questionStatus.eq(ACTIVE))
                .orderBy(questionHowabout.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<QuestionHowabout> query = jpaQueryFactory.selectFrom(questionHowabout)
                .where(questionHowabout.questionStatus.eq(ACTIVE))
                .orderBy(questionHowabout.createdAt.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }

    /**
     * QuestionRecommend만 조회. Ordering: createdAt Filtering : hashtag
     */
    @Override
    public Page<QuestionRecommend> getQuestionRecommendList(String hashtag, Pageable pageable) {
        List<QuestionRecommend> content = getQuestionRecommendTable(hashtag)
                .where(getQuestionRecommendFiltering(hashtag))
                .orderBy(questionRecommend.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<QuestionRecommend> query = getQuestionRecommendTable(hashtag)
                .where(getQuestionRecommendFiltering(hashtag))
                .orderBy(questionRecommend.createdAt.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetch().size());
    }

    @Override
    public Page<Question> getSearchQuestion(List<Long> questionIdList, Pageable pageable) {
        List<Question> content = jpaQueryFactory.selectFrom(question)
                .where(question.id.in(questionIdList)
                        .and(question.questionStatus.eq(ACTIVE))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(question.createdAt.desc()) // 추구 후현
                .fetch();

        //Count Query
        JPAQuery<Question> countJPAQuery = jpaQueryFactory.selectFrom(question)
                .where(question.id.in(questionIdList)
                        .and(question.questionStatus.eq(ACTIVE))
                )
                .orderBy(question.createdAt.desc());// 추구 후현

        return PageableExecutionUtils.getPage(content, pageable, () -> countJPAQuery.fetch().size());
    }

    private BooleanExpression getQuestionBuyFiltering(String voteStatus) {
        BooleanExpression predicate = questionBuy.questionStatus.eq(ACTIVE);
        LocalDateTime now = LocalDateTime.now();
        if (voteStatus == null) {
            return predicate;
        } else if (voteStatus.equals("진행 중")) {
            predicate = predicate.and(questionBuy.voteEndTime.after(now));
        } else if (voteStatus.equals("종료 임박")) {
            predicate = predicate.and(questionBuy.voteEndTime.between(now, now.plusDays(3)));
        } else if (voteStatus.equals("종료")) {
            predicate = predicate.and(questionBuy.voteEndTime.before(now));
        }

        return predicate;
    }

    private BooleanExpression getQuestionFindFiltering(Long celebId) {
        BooleanExpression predicate = questionFind.questionStatus.eq(ACTIVE);
        if (celebId == null) {
            return predicate;
        } else {
            predicate = predicate.and(
                    questionFind.celeb.id.eq(celebId)
                            .or(questionFind.celeb.parent.id.eq(celebId))
            );
        }

        return predicate;
    }

    private JPAQuery<QuestionRecommend> getQuestionRecommendTable(String hashtag) {
        JPAQuery<QuestionRecommend> select = jpaQueryFactory.select(questionRecommend);

        if (hashtag == null) {
            return select.from(questionRecommend);
        } else {
            return select.from(questionRecommend)
                    .leftJoin(questionRecommendCategory)
                    .on(questionRecommend.id.eq(questionRecommendCategory.question.id));
        }


    }

    private BooleanExpression getQuestionRecommendFiltering(String hashtag) {
        BooleanExpression predicate = questionRecommend.questionStatus.eq(ACTIVE);
        if (hashtag == null) {
            return predicate;
        } else {
            predicate = predicate.and(
                    questionRecommendCategory.name.eq(hashtag)
            );
        }

        return predicate;
    }


    private OrderSpecifier[] getQuestionBuyOrderSpecifier(String voteStatus) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        if (Objects.isNull(voteStatus)) {
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, questionBuy.createdAt));
        } else if (voteStatus.equals("종료 임박")) { // 종료 임박 순서
            orderSpecifiers.add(new OrderSpecifier(Order.ASC, questionBuy.voteEndTime));
        } else {
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, questionBuy.createdAt));
        }
        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }

    /**
     * Scheduler를 통한 일간 핫 Question 업데이트
     */
    @Override
    public List<Question> updateDailyHotQuestion() {
        return jpaQueryFactory.selectFrom(question)
                .leftJoin(questionLike).on(questionLike.question.id.eq(question.id))
                .leftJoin(comment).on(comment.question.id.eq(question.id))
                .groupBy(question)
                .where(question.questionStatus.eq(ACTIVE))
                .orderBy(questionLike.count().add(comment.count()).add(question.searchNum).desc())
                .limit(10)
                .fetch();
    }

    /**
     * 주간 Hot Question
     */
    @Override
    public Page<Question> getWeeklyHotQuestion(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();

        List<Question> content = jpaQueryFactory.select(question)
                .from(question)
                .leftJoin(questionLike).on(questionLike.question.id.eq(question.id))
                .leftJoin(comment).on(comment.question.id.eq(question.id))
                .where(
                        question.questionStatus.eq(ACTIVE)
                                .and(question.createdAt.between(now.minusDays(7).toLocalDate().atStartOfDay(),
                                        now.toLocalDate().atStartOfDay()))

                )
                .groupBy(question)
                .orderBy(question.searchNum.add(questionLike.count()).add(comment.count()).desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count Query
        JPAQuery<Question> countQuery = jpaQueryFactory.select(question)
                .from(question)
                .leftJoin(questionLike).on(questionLike.question.id.eq(question.id))
                .leftJoin(comment).on(comment.question.id.eq(question.id))
                .where(
                        question.questionStatus.eq(ACTIVE)
                                .and(question.createdAt.between(now.minusDays(7).toLocalDate().atStartOfDay(),
                                        now.toLocalDate().atStartOfDay()))
                )
                .groupBy(question);

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    @Override
    public List<Question> getDailyHotQuestion() {
        return jpaQueryFactory.select(question)
                .from(dailyHotQuestion)
                .leftJoin(question).on(dailyHotQuestion.question.eq(question)).fetchJoin()
                .where(question.questionStatus.eq(ACTIVE))
                .fetch();
    }

    @Override
    public List<Question> getSearchQuestionIds(String word) {
        List<Question> content = jpaQueryFactory.selectFrom(question)
                .where(question.title.like("%" + word + "%")
                        .or(question.content.like("%" + word + "%"))
                ).fetch();

        QCeleb parent = new QCeleb("parent");
        List<Question> questionFindContent = jpaQueryFactory.selectFrom(questionFind)
                .leftJoin(questionFind.celeb, celeb).fetchJoin()
                .leftJoin(questionFind.celeb.parent, parent).fetchJoin()
                .leftJoin(questionFind.newCeleb, newCeleb).fetchJoin()
                .where(questionFind.celeb.celebNameKr.like("%" + word + "%")
                        .or(questionFind.celeb.celebNameEn.like("%" + word + "%"))
                        .or(questionFind.newCeleb.celebName.like("%" + word + "%"))
                ).fetch().stream().map(question -> (Question) question).toList();

        return Collections.concat(content, questionFindContent);
    }

    @Override
    public List<QuestionBuy> getEndTimeBetweenNow(int voteEndCheckPeriod) {
        int periodToMinutes = voteEndCheckPeriod / 1000 / 60;
        LocalDateTime now = LocalDateTime.now();

        return jpaQueryFactory.selectFrom(questionBuy)
                .where(questionBuy.questionStatus.eq(ACTIVE)
                        .and(questionBuy.voteEndTime.between(now.minusMinutes(periodToMinutes), now))
                )
                .fetch();

    }
}
