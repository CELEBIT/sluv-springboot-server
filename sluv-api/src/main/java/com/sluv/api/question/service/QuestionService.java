package com.sluv.api.question.service;

import com.sluv.api.celeb.dto.response.CelebChipResponse;
import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.question.dto.*;
import com.sluv.api.question.helper.QuestionImgHelper;
import com.sluv.api.question.helper.QuestionItemHelper;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.celeb.service.CelebDomainService;
import com.sluv.domain.celeb.service.NewCelebDomainService;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.closet.service.ClosetDomainService;
import com.sluv.domain.comment.service.CommentDomainService;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.item.entity.ItemImg;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.item.service.ItemImgDomainService;
import com.sluv.domain.item.service.ItemScrapDomainService;
import com.sluv.domain.question.dto.QuestionImgSimpleDto;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.entity.*;
import com.sluv.domain.question.enums.QuestionStatus;
import com.sluv.domain.question.service.*;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserBlockDomainService;
import com.sluv.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionDomainService questionDomainService;
    private final QuestionImgDomainService questionImgDomainService;
    private final QuestionItemDomainService questionItemDomainService;
    private final QuestionRecommendCategoryDomainService questionRecommendCategoryDomainService;
    private final QuestionLikeDomainService questionLikeDomainService;
    private final CommentDomainService commentDomainService;
    private final ItemDomainService itemDomainService;
    private final ItemImgDomainService itemImgDomainService;
    private final CelebDomainService celebDomainService;
    private final NewCelebDomainService newCelebDomainService;
    private final RecentQuestionDomainService recentQuestionDomainService;
    private final ItemScrapDomainService itemScrapDomainService;
    private final ClosetDomainService closetDomainService;
    private final QuestionVoteDomainService questionVoteDomainService;
    private final UserDomainService userDomainService;
    private final UserBlockDomainService userBlockDomainService;

    private final QuestionImgHelper questionImgHelper;
    private final QuestionItemHelper questionItemHelper;
    private final QuestionVoteService questionVoteService;


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

        QuestionFind questionFind = QuestionFind.toEntity(user, dto.getId(), dto.getTitle(), dto.getContent(), celeb,
                newCeleb);

        // 2. QuestionFind 저장
        QuestionFind newQuestionFind = (QuestionFind) questionDomainService.saveQuestion(questionFind);

        // 3. QuestionImg 저장
        questionImgHelper.saveQuestionImg(dto.getImgList(), newQuestionFind);

        // 4. QuestionItem 저장
        questionItemHelper.saveQuestionItem(dto.getItemList(), newQuestionFind);

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
        QuestionBuy questionBuy = QuestionBuy.toEntity(user, dto.getId(), dto.getTitle(), dto.getVoteEndTime());

        // 2. QuestionBuy 저장
        QuestionBuy newQuestionBuy = (QuestionBuy) questionDomainService.saveQuestion(questionBuy);

        // 3. QuestionImg 저장
        questionImgHelper.saveQuestionImg(dto.getImgList(), newQuestionBuy);

        // 4. QuestionItem 저장
        questionItemHelper.saveQuestionItem(dto.getItemList(), newQuestionBuy);

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
        QuestionHowabout questionHowabout = QuestionHowabout.toEntity(user, dto.getId(), dto.getTitle(),
                dto.getContent());

        // 2. QuestionHotabout 저장
        QuestionHowabout newQuestionHowabout = (QuestionHowabout) questionDomainService.saveQuestion(questionHowabout);

        // 3. QuestionImg 저장
        questionImgHelper.saveQuestionImg(dto.getImgList(), newQuestionHowabout);

        // 4. QuestionItem 저장
        questionItemHelper.saveQuestionItem(dto.getItemList(), newQuestionHowabout);

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
        QuestionRecommend questionRecommend = QuestionRecommend.toEntity(user, dto.getId(), dto.getTitle(),
                dto.getContent());

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

        questionImgHelper.saveQuestionImg(dto.getImgList(), newQuestionRecommend);

        // 4. QuestionItem 저장
        questionItemHelper.saveQuestionItem(dto.getItemList(), newQuestionRecommend);

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

        String qType = getQuestionTypeOrNull(question);

        // 작성자
        User writer = userDomainService.findByIdOrNull(question.getUser().getId());

        // Question img List
        List<QuestionImgResDto> questionImgList = questionImgDomainService.findAllByQuestionId(questionId).
                stream()
                .map(questionImg -> {
                            QuestionVoteDataDto voteDataDto = null;
                            // QuestionBuy 라면
                            if (qType != null && qType.equals("Buy")) {
                                voteDataDto = questionVoteService.getVoteData(questionId, (long) questionImg.getSortOrder());
                            }

                            return QuestionImgResDto.of(questionImg, voteDataDto);
                        }
                ).toList();

        List<Closet> closets;

        if (nowUser != null) {
            closets = closetDomainService.findAllByUserId(nowUser.getId());
        } else {
            closets = new ArrayList<>();
        }

        // Question Item List
        List<QuestionItemResDto> questionItemList = questionItemDomainService.findAllByQuestionId(questionId)
                .stream()
                .map(questionItem -> {
                    ItemSimpleDto itemSimpleDto = ItemSimpleDto.of(
                            questionItem.getItem(),
                            itemImgDomainService.findMainImg(questionItem.getItem().getId()),
                            itemScrapDomainService.getItemScrapStatus(questionItem.getItem(), closets)
                    );
                    QuestionVoteDataDto questionVoteDataDto = null;
                    // QuestionBuy일 경우 투표수 추가.
                    if (qType != null && qType.equals("Buy")) {
                        questionVoteDataDto = questionVoteService.getVoteData(questionId, (long) questionItem.getSortOrder());
                    }

                    return QuestionItemResDto.of(questionItem, itemSimpleDto, questionVoteDataDto);
                }).toList();

        // Question Like Num Count
        Long questionLikeNum = questionLikeDomainService.countByQuestionId(questionId);

        // Question Comment Num Count
        Long questionCommentNum = commentDomainService.countByQuestionId(questionId);

        // hasLike 검색
        Boolean currentUserLike =
                nowUser != null && questionLikeDomainService.existsByQuestionIdAndUserId(questionId, nowUser.getId());

        CelebChipResponse celeb = null;
        CelebChipResponse newCeleb = null;
        LocalDateTime voteEndTime = null;
        Long totalVoteNum = null;
        Long voteStatus = null;
        List<String> recommendCategoryList = null;

        switch (qType) {
            case "Find" -> {
                QuestionFind questionFind = (QuestionFind) question;
                if (questionFind.getCeleb() != null) {
                    celeb = CelebChipResponse.of(questionFind.getCeleb());
                } else {
                    newCeleb = CelebChipResponse.of(questionFind.getNewCeleb());
                }
            }
            case "Buy" -> {
                QuestionBuy questionBuy = (QuestionBuy) question;
                QuestionVote questionVote =
                        nowUser != null ?
                                questionVoteDomainService.findByQuestionIdAndUserIdOrNull(questionId, nowUser.getId())
                                : null;
                voteEndTime = questionBuy.getVoteEndTime();
                totalVoteNum = questionVoteDomainService.countByQuestionId(questionId);
                voteStatus = questionVote != null
                        ? questionVote.getVoteSortOrder()
                        : null;
            }
            case "Recommend" ->
                    recommendCategoryList = questionRecommendCategoryDomainService.findAllByQuestionId(questionId)
                            .stream().map(QuestionRecommendCategory::getName).toList();
        }

        // RecentQuestion 등록
        if (nowUser != null) {
            recentQuestionDomainService.saveRecentQuestion(nowUser, qType, question);

            // SearchNum 증가
            increaseQuestionViewNum(nowUser.getId(), question);
        }

        return QuestionGetDetailResDto.of(
                question, qType, writer,
                questionImgList, questionItemList,
                questionLikeNum, questionCommentNum, currentUserLike,
                writer != null && nowUser != null && nowUser.getId().equals(writer.getId()),
                celeb, newCeleb,
                voteEndTime, totalVoteNum, voteStatus,
                recommendCategoryList
        );
    }

    private void increaseQuestionViewNum(Long userId, Question question) {
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

    @Transactional(readOnly = true)
    public PaginationResponse<QuestionBuySimpleResDto> getQuestionBuyList(Long userId, String voteStatus,
                                                                          Pageable pageable) {
        User user = userDomainService.findById(userId);
        List<Long> blockUserIds = new ArrayList<>();
        if (userId != null) {
            blockUserIds = userBlockDomainService.getAllBlockedUser(userId).stream()
                    .map(userBlock -> userBlock.getBlockedUser().getId())
                    .toList();
        }

        Page<QuestionBuy> questionPage = questionDomainService.getQuestionBuyList(voteStatus, blockUserIds, pageable);

        List<QuestionBuySimpleResDto> content = questionPage.stream().map(question -> {

            List<QuestionImgResDto> imgList = questionImgDomainService.findAllByQuestionId(question.getId())
                    .stream()
                    .map(questionImg -> {
                        QuestionVoteDataDto voteDataDto = questionVoteService.getVoteData(questionImg.getQuestion().getId(),
                                (long) questionImg.getSortOrder());

                        return QuestionImgResDto.of(questionImg, voteDataDto);
                    }).toList();

            // 아이템 이미지 URL
            List<QuestionItemResDto> itemImgList = questionItemDomainService.findAllByQuestionId(question.getId())
                    .stream()
                    .map(questionItem -> {
                        ItemSimpleDto itemSimpleDto = ItemSimpleDto.of(
                                questionItem.getItem(),
                                itemImgDomainService.findMainImg(questionItem.getItem().getId()),
                                null
                        );
                        QuestionVoteDataDto questionVoteDataDto = questionVoteService.getVoteData(questionItem.getQuestion().getId(),
                                (long) questionItem.getSortOrder());
                        return QuestionItemResDto.of(questionItem, itemSimpleDto, questionVoteDataDto);
                    }).toList();

            Long voteCount = questionVoteService.getTotalVoteCount(imgList, itemImgList);

            QuestionVote questionVote = null;
            if (user != null) {
                questionVote = questionVoteDomainService.findByQuestionIdAndUserIdOrNull(question.getId(),
                        user.getId());
            }

            User writer = userDomainService.findByIdOrNull(question.getUser().getId());

            return QuestionBuySimpleResDto.of(user, question, writer, voteCount, imgList, itemImgList,
                    question.getVoteEndTime(),
                    questionVote);
        }).toList();

        return PaginationResponse.of(questionPage, content);
    }

    /**
     * 일일 Hot Question 조회 기능.
     */
    @Transactional(readOnly = true)
    public List<QuestionHomeResDto> getDailyHotQuestionList(Long userId) {
        List<Long> blockUserIds = new ArrayList<>();
        if (userId != null) {
            blockUserIds = userBlockDomainService.getAllBlockedUser(userId).stream()
                    .map(userBlock -> userBlock.getBlockedUser().getId())
                    .toList();
        }

        List<Question> dailyHoyQuestionList = questionDomainService.getDailyHotQuestion(blockUserIds);

        List<QuestionHomeResDto> result = dailyHoyQuestionList.stream().map(question -> {
            List<QuestionImgSimpleDto> questionImgSimpleList = getQuestionImgSimpleList(question);
            User writer = userDomainService.findByIdOrNull(question.getUser().getId());
            return QuestionHomeResDto.of(question, writer, questionImgSimpleList);

        }).toList();

        return result;

    }

    private List<QuestionImgSimpleDto> getQuestionImgSimpleList(Question question) {

        // Question이 QuestionBuy인 경우 모든 이미지를 순서대로 조회
        if (question instanceof QuestionBuy) {
            List<QuestionImgSimpleDto> result = new ArrayList<>();

            List<QuestionImg> questionImgList = questionImgDomainService.findAllByQuestionId(question.getId());
            List<ItemImg> questionItemImgList = questionItemDomainService.findAllByQuestionId(question.getId()).stream()
                    .map(questionItem -> itemImgDomainService.findMainImg(questionItem.getItem().getId())).toList();

            questionImgList.forEach(questionImg -> result.add(QuestionImgSimpleDto.of(questionImg)));
            questionItemImgList.forEach(itemImg -> result.add(QuestionImgSimpleDto.of(itemImg)));

            result.sort(Comparator.comparing(QuestionImgSimpleDto::getSortOrder));

            return result;
        } else {// 그 외에는 대표이미지만 조화

            QuestionImg questionImg = questionImgDomainService.findByQuestionIdAndRepresentFlag(question.getId(), true);
            QuestionItem questionItem = questionItemDomainService.findByQuestionIdAndRepresentFlag(question.getId(),
                    true);

            String imgUrl = null;

            if (questionImg != null) {
                imgUrl = questionImg.getImgUrl();
            } else if (questionItem != null) {
                imgUrl = itemImgDomainService.findMainImg(questionItem.getItem().getId()).getItemImgUrl();
            }

            return imgUrl != null
                    ? Arrays.asList(new QuestionImgSimpleDto(imgUrl, 0L))
                    : null;
        }
    }

    /**
     * 주간 Hot Question 조회 기능.
     */
    @Transactional(readOnly = true)
    public PaginationResponse<QuestionSimpleResDto> getWeeklyHotQuestionList(Long userId, Pageable pageable) {
        List<Long> blockUserIds = new ArrayList<>();
        if (userId != null) {
            blockUserIds = userBlockDomainService.getAllBlockedUser(userId).stream()
                    .map(userBlock -> userBlock.getBlockedUser().getId())
                    .toList();
        }

        Page<Question> page = questionDomainService.getWeeklyHotQuestion(blockUserIds, pageable);

        List<QuestionSimpleResDto> content = page.stream().map(question -> {
            List<String> categoryList = null;
            if (question instanceof QuestionRecommend) {
                categoryList = questionRecommendCategoryDomainService.findAllByQuestionId(question.getId())
                        .stream()
                        .map(QuestionRecommendCategory::getName).toList();
            }

            List<QuestionImgSimpleDto> imgList = questionImgDomainService.findAllByQuestionId(question.getId())
                    .stream()
                    .map(QuestionImgSimpleDto::of)
                    .toList();

            List<QuestionImgSimpleDto> itemImgList = questionItemDomainService.findAllByQuestionId(question.getId())
                    .stream()
                    .map(questionItem ->
                            QuestionImgSimpleDto.of(itemImgDomainService.findMainImg(questionItem.getItem().getId()))
                    )
                    .toList();

            Long commentNum = commentDomainService.countByQuestionId(question.getId());
            Long likeNum = questionLikeDomainService.countByQuestionId(question.getId());
            User writer = userDomainService.findByIdOrNull(question.getUser().getId());

            return QuestionSimpleResDto.of(question, writer, likeNum, commentNum, imgList, itemImgList, categoryList);
        }).toList();

        return PaginationResponse.of(page, content);
    }
}
