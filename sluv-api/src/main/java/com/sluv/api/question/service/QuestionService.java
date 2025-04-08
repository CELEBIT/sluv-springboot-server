package com.sluv.api.question.service;

import com.sluv.api.celeb.dto.response.CelebChipResponse;
import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.question.dto.*;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.celeb.service.CelebDomainService;
import com.sluv.domain.celeb.service.NewCelebDomainService;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.closet.service.ClosetDomainService;
import com.sluv.domain.comment.service.CommentDomainService;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemImg;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.item.service.ItemImgDomainService;
import com.sluv.domain.item.service.ItemScrapDomainService;
import com.sluv.domain.question.dto.QuestionImgSimpleDto;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.entity.*;
import com.sluv.domain.question.enums.QuestionStatus;
import com.sluv.domain.question.exception.QuestionReportDuplicateException;
import com.sluv.domain.question.exception.QuestionTypeNotFoundException;
import com.sluv.domain.question.service.*;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserBlockDomainService;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.infra.alarm.service.QuestionAlarmService;
import com.sluv.infra.counter.view.ViewCounter;
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
    private final QuestionReportDomainService questionReportDomainService;
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

    private final ViewCounter viewCounter;
    private final QuestionAlarmService questionAlarmService;


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
        postQuestionImgs(dto.getImgList(), newQuestionFind);

        // 4. QuestionItem 저장
        postQuestionItems(dto.getItemList(), newQuestionFind);

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
        postQuestionImgs(dto.getImgList(), newQuestionBuy);

        // 4. QuestionItem 저장
        postQuestionItems(dto.getItemList(), newQuestionBuy);

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
        postQuestionImgs(dto.getImgList(), newQuestionHowabout);

        // 4. QuestionItem 저장
        postQuestionItems(dto.getItemList(), newQuestionHowabout);

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

        postQuestionImgs(dto.getImgList(), newQuestionRecommend);

        // 4. QuestionItem 저장
        postQuestionItems(dto.getItemList(), newQuestionRecommend);

        return QuestionPostResDto.of(newQuestionRecommend.getId());
    }

    /**
     * Question Img 저장 메소드
     */
    private void postQuestionImgs(List<QuestionImgReqDto> dtoList, Question question) {
        // Question에 대한 Img 초기화
        questionImgDomainService.deleteAllByQuestionId(question.getId());

        if (dtoList != null) {
            // Question Img들 추가
            List<QuestionImg> imgList = dtoList.stream()
                    .map(imgDto -> QuestionImg.toEntity(question, imgDto.getImgUrl(), imgDto.getDescription(),
                            imgDto.getRepresentFlag(), imgDto.getSortOrder()))
                    .toList();

            questionImgDomainService.saveAll(imgList);
        }
    }

    /**
     * Questim Item 저장 메소드
     */
    private void postQuestionItems(List<QuestionItemReqDto> dtoList, Question question) {
        // Question에 대한 Item 초기화
        questionItemDomainService.deleteAllByQuestionId(question.getId());
        if (dtoList != null) {
            // Question Item들 추가
            List<QuestionItem> items = dtoList.stream().map(itemDto -> {
                        Item item = itemDomainService.findById(itemDto.getItemId());
                        return QuestionItem.toEntity(question, item, itemDto.getDescription(), itemDto.getRepresentFlag(),
                                itemDto.getSortOrder());
                    }
            ).toList();

            questionItemDomainService.saveAll(items);
        }
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        log.info("질문 게시글 삭제 - 질문 게시글 : {}", questionId);
        Question question = questionDomainService.findById(questionId);
        question.changeQuestionStatus(QuestionStatus.DELETED);
    }

    @Transactional
    public void postQuestionLike(Long userId, Long questionId) {
        User user = userDomainService.findById(userId);
        log.info("질문 게시글 좋아요 - 사용자 : {}, 질문 게시글 : {}", user.getId(), questionId);
        // 해당 유저의 Question 게시물 like 여부 검색
        Boolean likeStatus = questionLikeDomainService.existsByQuestionIdAndUserId(questionId, user.getId());
        Question question = questionDomainService.findById(questionId);

        if (likeStatus) {
            // like가 있다면 삭제
            questionLikeDomainService.deleteByQuestionIdAndUserId(questionId, user.getId());
        } else {
            // like가 없다면 등록
            questionLikeDomainService.saveQuestionLike(user, question);
            questionAlarmService.sendAlarmAboutQuestionLike(user.getId(), question.getId());
        }

    }

    @Transactional
    public void postQuestionReport(Long userId, Long questionId, QuestionReportReqDto dto) {
        User user = userDomainService.findById(userId);
        log.info("질문 게시글 신고 - 사용자 : {}, 질문 게시글 : {}, 사유 : {}", user.getId(), questionId, dto.getReason());
        Boolean reportExist = questionReportDomainService.existsByQuestionIdAndReporterId(questionId, user.getId());

        if (!reportExist) {
            // 신고 내역이 없다면 신고 등록.
            Question question = questionDomainService.findById(questionId);

            questionReportDomainService.saveQuestionReport(user, question, dto.getReason(), dto.getContent());

        } else {
            // 신고 내역이 있다면 중복 신고 방지.
            throw new QuestionReportDuplicateException();
        }
    }

    @Transactional
    public QuestionGetDetailResDto getQuestionDetail(Long nowUserId, Long questionId) {
        Question question = questionDomainService.findById(questionId);
        User nowUser = userDomainService.findById(nowUserId);

        // Question Type 분류
        String qType;
        if (question != null) {
            if (question instanceof QuestionFind) {
                qType = "Find";
            } else if (question instanceof QuestionBuy) {
                qType = "Buy";
            } else if (question instanceof QuestionHowabout) {
                qType = "How";
            } else if (question instanceof QuestionRecommend) {
                qType = "Recommend";
            } else {
                qType = null;
            }
        } else {
            qType = null;
        }

        // 작성자
        User writer = userDomainService.findByIdOrNull(question.getUser().getId());

        // Question img List
        List<QuestionImgResDto> questionImgList = questionImgDomainService.findAllByQuestionId(questionId).
                stream()
                .map(questionImg -> {
                            QuestionVoteDataDto voteDataDto = null;
                            // QuestionBuy 라면
                            if (qType != null && qType.equals("Buy")) {
                                voteDataDto = getVoteData(questionId, (long) questionImg.getSortOrder());
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
                    if (qType != null & qType.equals("Buy")) {
                        questionVoteDataDto = getVoteData(questionId, (long) questionItem.getSortOrder());
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
        boolean isExist = viewCounter.existUserViewQuestionId(userId, question.getId());
        if (!isExist) {
            viewCounter.saveUserViewQuestionId(userId, question.getId());
            question.increaseSearchNum();
        }
    }

    /**
     * builder에 VoteNum, VotePercent 탑재
     */
    private QuestionVoteDataDto getVoteData(Long questionId, Long sortOrder) {
        List<QuestionVote> questionVotes = questionVoteDomainService.findAllByQuestionId(questionId);

        // 해당 SortOrder의 투표 수
        Long voteNum = 0L;
        for (QuestionVote questionVote : questionVotes) {
            if (Objects.equals(questionVote.getVoteSortOrder(), sortOrder)) {
                voteNum++;
            }
        }

        return QuestionVoteDataDto.of(
                voteNum,
                questionVotes.size() != 0
                        ? getVotePercent(voteNum, questionVotes.stream().count())
                        : 0
        );
    }

    /**
     * QuestionVote 퍼센트 계산.
     */
    private Double getVotePercent(Long voteNum, Long totalVoteNum) {
        double div = (double) voteNum / (double) totalVoteNum;
        return Math.round(div * 1000) / 10.0;
    }

    /**
     * QuestionBuy 등록 및 취소
     */
    @Transactional
    public void postQuestionVote(Long userId, Long questionId, QuestionVoteReqDto dto) {
        User user = userDomainService.findById(userId);
        log.info("질문 게시글 투표 - 사용자 : {}, 질문 게시글 : {}, 투표 : {}", user.getId(), questionId, dto.getVoteSortOrder());
        QuestionVote questionVote = questionVoteDomainService.findByQuestionIdAndUserIdOrNull(questionId, user.getId());

        if (questionVote == null) {
            // 투표 등록

            // Question 검색
            Question question = questionDomainService.findById(questionId);

            // QuestionVote 생성 및 저장
            questionVoteDomainService.saveQuestionVote(question, user, dto.getVoteSortOrder());

        } else {
            // 투표 취소

            // 해당 QuestionVote 삭제.
            questionVoteDomainService.deleteById(questionVote.getId());
        }
    }

    /**
     * Question 상세보기 하단의 추천 게시글
     */
    @Transactional(readOnly = true)
    public List<QuestionSimpleResDto> getWaitQuestionBuy(Long userId, Long questionId) {
        User user = userDomainService.findById(userId);
        List<Long> blockUserIds = userBlockDomainService.getAllBlockedUser(userId).stream()
                .map(userBlock -> userBlock.getBlockedUser().getId())
                .toList();

        List<Celeb> interestedCelebs = new ArrayList<>();
        if (user != null) {
            interestedCelebs = celebDomainService.findInterestedCeleb(user);
        }

        return questionDomainService.getWaitQuestionBuy(user, questionId, interestedCelebs, blockUserIds)
                .stream()
                .map(questionBuy -> getQuestionSimpleResDto(questionBuy, "Buy"))
                .toList();
    }

    /**
     * Wait QuestionRecommend 조회
     */
    @Transactional(readOnly = true)
    public List<QuestionSimpleResDto> getWaitQuestionRecommend(Long userId, Long questionId) {
        User user = userDomainService.findById(userId);
        List<Long> blockUserIds = userBlockDomainService.getAllBlockedUser(userId).stream()
                .map(userBlock -> userBlock.getBlockedUser().getId())
                .toList();

        return questionDomainService.getWaitQuestionRecommend(user, questionId, blockUserIds)
                .stream()
                .map(questionRecommend -> getQuestionSimpleResDto(questionRecommend, "Recommend"))
                .toList();
    }

    /**
     * 가
     * Wait QuestionHowabout 조회
     */
    @Transactional(readOnly = true)
    public List<QuestionSimpleResDto> getWaitQuestionHowabout(Long userId, Long questionId) {
        User user = userDomainService.findById(userId);
        List<Long> blockUserIds = userBlockDomainService.getAllBlockedUser(userId).stream()
                .map(userBlock -> userBlock.getBlockedUser().getId())
                .toList();

        return questionDomainService.getWaitQuestionHowabout(user, questionId, blockUserIds)
                .stream()
                .map(questionHowabout -> getQuestionSimpleResDto(questionHowabout, "How"))
                .toList();
    }

    /**
     * Wait QuestionFind 조회
     */
    @Transactional(readOnly = true)
    public List<QuestionSimpleResDto> getWaitQuestionFind(Long userId, Long questionId) {
        User user = userDomainService.findById(userId);
        List<Long> blockUserIds = userBlockDomainService.getAllBlockedUser(userId).stream()
                .map(userBlock -> userBlock.getBlockedUser().getId())
                .toList();

        List<Celeb> interestedCelebs = new ArrayList<>();

        if (user != null) {
            interestedCelebs = celebDomainService.findInterestedCeleb(user);
        }

        return questionDomainService.getWaitQuestionFind(user, questionId, interestedCelebs, blockUserIds)
                .stream()
                .map(questionFind -> getQuestionSimpleResDto(questionFind, "Find"))
                .toList();
    }

    public QuestionSimpleResDto getQuestionSimpleResDto(Question question, String qType) {
        List<QuestionImgSimpleDto> imgList = new ArrayList<>();
        List<QuestionImgSimpleDto> itemImgList = new ArrayList<>();
        String celebName = null;
        List<String> categoryNameList = null;

//        if (!qType.equals("Buy")) {
        if (!(question instanceof QuestionBuy)) {
            // 이미지 URL
            QuestionImg questionImg = questionImgDomainService.findByQuestionIdAndRepresentFlag(question.getId(), true);

            // 이미지 Dto로 변경
            if (questionImg != null) {
                imgList.add(QuestionImgSimpleDto.of(questionImg));
            }

            // 아이템 이미지 URL
            QuestionItem questionItem = questionItemDomainService.findByQuestionIdAndRepresentFlag(question.getId(),
                    true);

            // 아이템 이미지 Dto로 변경
            if (questionItem != null) {
                ItemImg mainImg = itemImgDomainService.findMainImg(questionItem.getItem().getId());
                imgList.add(QuestionImgSimpleDto.of(mainImg));
            }

        } else {
            // 이미지 URL
            imgList = questionImgDomainService.findAllByQuestionId(question.getId())
                    .stream()
                    .map(QuestionImgSimpleDto::of).toList();

            // 아이템 이미지 URL
            itemImgList = questionItemDomainService.findAllByQuestionId(question.getId())
                    .stream()
                    .map(item -> {
                        ItemImg mainImg = itemImgDomainService.findMainImg(item.getItem().getId());
                        return QuestionImgSimpleDto.of(mainImg);
                    }).toList();
        }

//        if (qType.equals("Recommend")) {
        if (question instanceof QuestionRecommend) {
            categoryNameList = questionRecommendCategoryDomainService.findAllByQuestionId(question.getId()).stream()
                    .map(QuestionRecommendCategory::getName).toList();
        }

        // Question 좋아요 수
        Long likeNum = questionLikeDomainService.countByQuestionId(question.getId());

        // Question 댓글 수
        Long commentNum = commentDomainService.countByQuestionId(question.getId());

        User writer = userDomainService.findByIdOrNull(question.getUser().getId());

        return QuestionSimpleResDto.of(question, writer, likeNum, commentNum,
                imgList, itemImgList, categoryNameList);
    }

    /**
     * Question 리스트를 최신순으로 조회
     */
    @Transactional(readOnly = true)
    public PaginationResponse<QuestionSimpleResDto> getTotalQuestionList(Pageable pageable) {
        Page<Question> questionPage = questionDomainService.getTotalQuestionList(pageable);
        List<QuestionSimpleResDto> content = questionPage.stream().map(question -> {
            String qType;
            if (question instanceof QuestionBuy) {
                qType = "Buy";
            } else if (question instanceof QuestionFind) {
                qType = "Find";
            } else if (question instanceof QuestionHowabout) {
                qType = "Howabout";
            } else if (question instanceof QuestionRecommend) {
                qType = "Recommend";
            } else {
                throw new QuestionTypeNotFoundException();
            }
            return getQuestionSimpleResDto(question, qType);
        }).toList();

        return PaginationResponse.create(questionPage, content);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<QuestionBuySimpleResDto> getQuestionBuyList(Long userId, String voteStatus,
                                                                          Pageable pageable) {
        User user = userDomainService.findById(userId);
        Page<QuestionBuy> questionPage = questionDomainService.getQuestionBuyList(voteStatus, pageable);

        List<QuestionBuySimpleResDto> content = questionPage.stream().map(question -> {

            List<QuestionImgResDto> imgList = questionImgDomainService.findAllByQuestionId(question.getId())
                    .stream()
                    .map(questionImg -> {
                        QuestionVoteDataDto voteDataDto = getVoteData(questionImg.getQuestion().getId(),
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
                        QuestionVoteDataDto questionVoteDataDto = getVoteData(questionItem.getQuestion().getId(),
                                (long) questionItem.getSortOrder());
                        return QuestionItemResDto.of(questionItem, itemSimpleDto, questionVoteDataDto);
                    }).toList();

            Long voteCount = getTotalVoteCount(imgList, itemImgList);

            QuestionVote questionVote = questionVoteDomainService.findByQuestionIdAndUserIdOrNull(question.getId(),
                    user.getId());

            User writer = userDomainService.findByIdOrNull(question.getUser().getId());

            return QuestionBuySimpleResDto.of(user, question, writer, voteCount, imgList, itemImgList,
                    question.getVoteEndTime(),
                    questionVote);
        }).toList();

        return PaginationResponse.create(questionPage, content);
    }

    private Long getTotalVoteCount(List<QuestionImgResDto> imgList, List<QuestionItemResDto> itemImgList) {
        long totalVoteCount = 0L;

        for (QuestionImgResDto questionImgResDto : imgList) {
            totalVoteCount += questionImgResDto.getVoteNum();
        }

        for (QuestionItemResDto questionItemResDto : itemImgList) {
            totalVoteCount += questionItemResDto.getVoteNum();
        }

        return totalVoteCount;
    }

    /**
     * QuestionFind 커뮤니티 게시글 조회.
     */
    @Transactional(readOnly = true)
    public PaginationResponse<QuestionSimpleResDto> getQuestionFindList(Long celebId, Pageable pageable) {

        Page<QuestionFind> questionPage = questionDomainService.getQuestionFindList(celebId, pageable);

        List<QuestionSimpleResDto> content = questionPage.stream().map(question ->
                getQuestionSimpleResDto(question, "Find")
        ).toList();

        return PaginationResponse.create(questionPage, content);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<QuestionSimpleResDto> getQuestionHowaboutList(Pageable pageable) {
        Page<QuestionHowabout> questionPage = questionDomainService.getQuestionHowaboutList(pageable);
        List<QuestionSimpleResDto> content = questionPage.stream().map(question ->
                getQuestionSimpleResDto(question, "How")
        ).toList();

        return PaginationResponse.create(questionPage, content);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<QuestionSimpleResDto> getQuestionRecommendList(String hashtag, Pageable pageable) {
        Page<QuestionRecommend> questionPage = questionDomainService.getQuestionRecommendList(hashtag, pageable);
        List<QuestionSimpleResDto> content = questionPage.stream().map(question ->
                getQuestionSimpleResDto(question, "Recommend")
        ).toList();

        return PaginationResponse.create(questionPage, content);
    }

    private String getQuestionCelebName(QuestionFind questionFind) {
        return questionFind.getCeleb() != null
                ? questionFind.getCeleb().getParent() != null
                ? questionFind.getCeleb().getParent().getCelebNameKr() + " " + questionFind.getCeleb().getCelebNameKr()
                : questionFind.getCeleb().getCelebNameKr()
                : questionFind.getNewCeleb().getCelebName();
    }

    /**
     * 일일 Hot Question 조회 기능.
     */
    @Transactional(readOnly = true)
    public List<QuestionHomeResDto> getDailyHotQuestionList() {
        List<Question> dailyHoyQuestionList = questionDomainService.getDailyHotQuestion();

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
    public PaginationResponse<QuestionSimpleResDto> getWeeklyHotQuestionList(Pageable pageable) {
        Page<Question> page = questionDomainService.getWeeklyHotQuestion(pageable);

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

        return PaginationResponse.create(page, content);
    }
}

