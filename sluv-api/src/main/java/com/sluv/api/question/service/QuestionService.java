package com.sluv.api.question.service;

import com.sluv.api.celeb.dto.response.CelebChipResponse;
import com.sluv.api.moderation.service.QuestionModerationService;
import com.sluv.api.moderation.service.QuestionModerationStatusService;
import com.sluv.api.question.dto.*;
import com.sluv.api.question.helper.QuestionImageManager;
import com.sluv.api.question.helper.QuestionItemManager;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.celeb.service.CelebDomainService;
import com.sluv.domain.celeb.service.NewCelebDomainService;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.closet.service.ClosetDomainService;
import com.sluv.domain.comment.service.CommentDomainService;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.entity.*;
import com.sluv.domain.question.enums.QuestionStatus;
import com.sluv.domain.question.service.*;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionDomainService questionDomainService;
    private final QuestionRecommendCategoryDomainService questionRecommendCategoryDomainService;
    private final QuestionLikeDomainService questionLikeDomainService;
    private final CommentDomainService commentDomainService;
    private final CelebDomainService celebDomainService;
    private final NewCelebDomainService newCelebDomainService;
    private final RecentQuestionDomainService recentQuestionDomainService;
    private final ClosetDomainService closetDomainService;
    private final QuestionVoteDomainService questionVoteDomainService;
    private final UserDomainService userDomainService;

    private final QuestionImageManager questionImageManager;
    private final QuestionItemManager questionItemManager;
    private final QuestionVoteService questionVoteService;
    private final QuestionModerationService questionModerationService;
    private final QuestionModerationStatusService questionModerationStatusService;


    @Transactional
    public QuestionPostResDto postQuestionFind(Long userId, QuestionFindPostReqDto dto) {
        /**
         * 1. 생성 or 수정
         * 2. QuestionFind 저장
         * 3. QuestionImg 저장
         * 4. QuestionItem 저장
         */
        User user = userDomainService.findById(userId);
        log.info("찾아주세요 게시글 등록 or 수정 - 사용자 : {}, 질문 게시글 : {}, 질문 게시글 제목 : {}",
                user.getId(), dto.getId() == null ? null : dto.getId(), dto.getTitle());
        // 1. 생성 or 수정
        Celeb celeb = null;
        if (dto.getCelebId() != null) {
            celeb = celebDomainService.findByIdOrNull(dto.getCelebId());
        }
        NewCeleb newCeleb = null;
        if (dto.getNewCelebId() != null) {
            newCeleb = newCelebDomainService.findByNewCelebIdOrNull(dto.getNewCelebId());
        }

        QuestionStatus questionStatus = getQuestionStatus(dto.getId());
        QuestionFind questionFind = QuestionFind.toEntity(user, dto.getId(), dto.getTitle(), dto.getContent(), celeb,
                newCeleb, questionStatus);

        // 2. QuestionFind 저장
        QuestionFind newQuestionFind = (QuestionFind) questionDomainService.saveQuestion(questionFind);

        // 3. QuestionImg 저장
        questionImageManager.saveImages(dto.getImgList(), newQuestionFind);

        // 4. QuestionItem 저장
        questionItemManager.saveItems(dto.getItemList(), newQuestionFind);

        questionModerationService.createQuestionJobIfEnabled(newQuestionFind);

        return QuestionPostResDto.of(newQuestionFind.getId());

    }

    @Transactional
    public QuestionPostResDto postQuestionBuy(Long userId, QuestionBuyPostReqDto dto) {
        /**
         * 1. 생성 or 수정
         * 2. QuestionBuy 저장
         * 3. QuestionImg 저장
         * 4. QuestionItem 저장
         */
        User user = userDomainService.findById(userId);
        log.info("이 중에 뭐 살까 게시글 등록 or 수정 - 사용자 : {}, 질문 게시글 : {}, 질문 게시글 제목 : {}",
                user.getId(), dto.getId() == null ? null : dto.getId(), dto.getTitle());

        // 1. 생성 or 수정
        QuestionStatus questionStatus = getQuestionStatus(dto.getId());
        QuestionBuy questionBuy = QuestionBuy.toEntity(user, dto.getId(), dto.getTitle(), dto.getVoteEndTime(),
                questionStatus);

        // 2. QuestionBuy 저장
        QuestionBuy newQuestionBuy = (QuestionBuy) questionDomainService.saveQuestion(questionBuy);

        // 3. QuestionImg 저장
        questionImageManager.saveImages(dto.getImgList(), newQuestionBuy);

        // 4. QuestionItem 저장
        questionItemManager.saveItems(dto.getItemList(), newQuestionBuy);

        questionModerationService.createQuestionJobIfEnabled(newQuestionBuy);

        return QuestionPostResDto.of(newQuestionBuy.getId());
    }

    @Transactional
    public QuestionPostResDto postQuestionHowabout(Long userId, QuestionHowaboutPostReqDto dto) {
        /**
         * 1. 생성 or 수정
         * 2. QuestionHowabout 저장
         * 3. QuestionImg 저장
         * 4. QuestionItem 저장
         */
        User user = userDomainService.findById(userId);
        log.info("이거 어때 게시글 등록 or 수정 - 사용자 : {}, 질문 게시글 : {}, 질문 게시글 제목 : {}",
                user.getId(), dto.getId() == null ? null : dto.getId(), dto.getTitle());

        // 1. 생성 or 수정
        QuestionStatus questionStatus = getQuestionStatus(dto.getId());
        QuestionHowabout questionHowabout = QuestionHowabout.toEntity(user, dto.getId(), dto.getTitle(),
                dto.getContent(), questionStatus);

        // 2. QuestionHotabout 저장
        QuestionHowabout newQuestionHowabout = (QuestionHowabout) questionDomainService.saveQuestion(questionHowabout);

        // 3. QuestionImg 저장
        questionImageManager.saveImages(dto.getImgList(), newQuestionHowabout);

        // 4. QuestionItem 저장
        questionItemManager.saveItems(dto.getItemList(), newQuestionHowabout);

        questionModerationService.createQuestionJobIfEnabled(newQuestionHowabout);

        return QuestionPostResDto.of(newQuestionHowabout.getId());
    }

    @Transactional
    public QuestionPostResDto postQuestionRecommend(Long userId, QuestionRecommendPostReqDto dto) {
        /**
         * 1. 생성 or 수정
         * 1. QuestionRecommend 저장
         * 2. Recommend Category 저장
         * 3. QuestionImg 저장
         * 4. QuestionItem 저장
         */
        User user = userDomainService.findById(userId);
        log.info("추천해줘 게시글 등록 or 수정 - 사용자 : {}, 질문 게시글 : {}, 질문 게시글 제목 : {}",
                user.getId(), dto.getId() == null ? null : dto.getId(), dto.getTitle());
        // 1. 생성 or 수정
        QuestionStatus questionStatus = getQuestionStatus(dto.getId());
        QuestionRecommend questionRecommend = QuestionRecommend.toEntity(user, dto.getId(), dto.getTitle(),
                dto.getContent(), questionStatus);

        // 2. QuestionRecommend 저장
        QuestionRecommend newQuestionRecommend = (QuestionRecommend) questionDomainService.saveQuestion(
                questionRecommend);

        // 3. Recommend Category 저장
        // Question에 대한 RecommendCategory 초기화
        questionRecommendCategoryDomainService.deleteAllByQuestionId(newQuestionRecommend.getId());

        List<QuestionRecommendCategory> recommendCategories = dto.getCategoryNameList().stream()
                .map(categoryName ->
                        QuestionRecommendCategory.toEntity(newQuestionRecommend, categoryName)
                ).toList();

        questionRecommendCategoryDomainService.saveAll(recommendCategories);

        // 4. QuestionImg 저장
        questionImageManager.saveImages(dto.getImgList(), newQuestionRecommend);

        // 5. QuestionItem 저장
        questionItemManager.saveItems(dto.getItemList(), newQuestionRecommend);

        questionModerationService.createQuestionJobIfEnabled(newQuestionRecommend);

        return QuestionPostResDto.of(newQuestionRecommend.getId());
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        log.info("질문 게시글 삭제 - 질문 게시글 : {}", questionId);
        Question question = questionDomainService.findById(questionId);
        question.changeQuestionStatus(QuestionStatus.DELETED);
    }

    @Transactional
    public QuestionGetDetailResDto getQuestionDetail(Long nowUserId, Long questionId) {
        Question question = questionDomainService.findById(questionId);
        User nowUser = userDomainService.findById(nowUserId);
        String questionType = getQuestionTypeOrNull(question);
        User writer = userDomainService.findByIdOrNull(question.getUser().getId());
        List<Closet> closets = getClosets(nowUser);

        List<QuestionImgResDto> questionImages = questionImageManager.getQuestionImageResponses(
                questionId,
                getVoteDataResolver(questionId, questionType)
        );
        List<QuestionItemResDto> questionItems = questionItemManager.getQuestionItemResponses(
                questionId,
                closets,
                getVoteDataResolver(questionId, questionType)
        );
        Long questionLikeNum = questionLikeDomainService.countByQuestionId(questionId);
        Long questionCommentNum = commentDomainService.countByQuestionId(questionId);
        Boolean currentUserLike = hasCurrentUserLike(nowUser, questionId);
        QuestionDetailTypeData typeData = getQuestionDetailTypeData(question, questionType, nowUser);

        if (nowUser != null) {
            recentQuestionDomainService.saveRecentQuestion(nowUser, questionType, question);
            increaseQuestionViewNum(question);
        }

        return QuestionGetDetailResDto.of(
                question, questionType, writer,
                questionImages, questionItems,
                questionLikeNum, questionCommentNum, currentUserLike,
                writer != null && nowUser != null && nowUser.getId().equals(writer.getId()),
                typeData.celeb(), typeData.newCeleb(),
                typeData.voteEndTime(), typeData.totalVoteNum(), typeData.voteStatus(),
                typeData.recommendCategories()
        );
    }

    private List<Closet> getClosets(User user) {
        if (user == null) {
            return List.of();
        }

        return closetDomainService.findAllByUserId(user.getId());
    }

    private QuestionStatus getQuestionStatus(Long questionId) {
        if (questionId == null) {
            return questionModerationStatusService.getInitialQuestionStatus();
        }

        return questionModerationStatusService.getUpdateQuestionStatus();
    }

    private Function<Integer, QuestionVoteDataDto> getVoteDataResolver(Long questionId, String questionType) {
        if (!"Buy".equals(questionType)) {
            return sortOrder -> null;
        }

        return sortOrder -> questionVoteService.getVoteData(questionId, sortOrder.longValue());
    }

    private Boolean hasCurrentUserLike(User nowUser, Long questionId) {
        return nowUser != null && questionLikeDomainService.existsByQuestionIdAndUserId(questionId, nowUser.getId());
    }

    private QuestionDetailTypeData getQuestionDetailTypeData(Question question, String questionType, User nowUser) {
        return switch (questionType) {
            case "Find" -> getFindQuestionDetailData((QuestionFind) question);
            case "Buy" -> getBuyQuestionDetailData((QuestionBuy) question, nowUser);
            case "Recommend" -> getRecommendQuestionDetailData(question.getId());
            default -> QuestionDetailTypeData.empty();
        };
    }

    private QuestionDetailTypeData getFindQuestionDetailData(QuestionFind question) {
        CelebChipResponse celeb = null;
        CelebChipResponse newCeleb = null;

        if (question.getCeleb() != null) {
            celeb = CelebChipResponse.of(question.getCeleb());
        } else {
            newCeleb = CelebChipResponse.of(question.getNewCeleb());
        }

        return QuestionDetailTypeData.ofCeleb(celeb, newCeleb);
    }

    private QuestionDetailTypeData getBuyQuestionDetailData(QuestionBuy question, User nowUser) {
        QuestionVote questionVote = nowUser != null
                ? questionVoteDomainService.findByQuestionIdAndUserIdOrNull(question.getId(), nowUser.getId())
                : null;
        Long voteStatus = questionVote != null
                ? questionVote.getVoteSortOrder()
                : null;

        return QuestionDetailTypeData.ofVote(
                question.getVoteEndTime(),
                questionVoteDomainService.countByQuestionId(question.getId()),
                voteStatus
        );
    }

    private QuestionDetailTypeData getRecommendQuestionDetailData(Long questionId) {
        List<String> recommendCategories = questionRecommendCategoryDomainService.findAllByQuestionId(questionId)
                .stream()
                .map(QuestionRecommendCategory::getName)
                .toList();

        return QuestionDetailTypeData.ofRecommendCategories(recommendCategories);
    }

    private void increaseQuestionViewNum(Question question) {
        question.increaseSearchNum();
    }

    private String getQuestionTypeOrNull(Question question) {
        if (question instanceof QuestionFind) {
            return "Find";
        } else if (question instanceof QuestionBuy) {
            return "Buy";
        } else if (question instanceof QuestionHowabout) {
            return "How";
        } else if (question instanceof QuestionRecommend) {
            return "Recommend";
        }
        return null;
    }

}
