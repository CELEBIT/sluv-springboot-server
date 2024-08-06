package com.sluv.server.domain.question.service;

import com.sluv.server.domain.alarm.service.QuestionAlarmService;
import com.sluv.server.domain.celeb.dto.CelebChipResDto;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.celeb.repository.NewCelebRepository;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.repository.ClosetRepository;
import com.sluv.server.domain.comment.repository.CommentRepository;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.ItemImg;
import com.sluv.server.domain.item.exception.ItemNotFoundException;
import com.sluv.server.domain.item.repository.ItemImgRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.item.repository.ItemScrapRepository;
import com.sluv.server.domain.question.dto.QuestionBuyPostReqDto;
import com.sluv.server.domain.question.dto.QuestionBuySimpleResDto;
import com.sluv.server.domain.question.dto.QuestionFindPostReqDto;
import com.sluv.server.domain.question.dto.QuestionGetDetailResDto;
import com.sluv.server.domain.question.dto.QuestionHomeResDto;
import com.sluv.server.domain.question.dto.QuestionHowaboutPostReqDto;
import com.sluv.server.domain.question.dto.QuestionImgReqDto;
import com.sluv.server.domain.question.dto.QuestionImgResDto;
import com.sluv.server.domain.question.dto.QuestionImgSimpleResDto;
import com.sluv.server.domain.question.dto.QuestionItemReqDto;
import com.sluv.server.domain.question.dto.QuestionItemResDto;
import com.sluv.server.domain.question.dto.QuestionPostResDto;
import com.sluv.server.domain.question.dto.QuestionRecommendPostReqDto;
import com.sluv.server.domain.question.dto.QuestionReportReqDto;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.question.dto.QuestionVoteDataDto;
import com.sluv.server.domain.question.dto.QuestionVoteReqDto;
import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.question.entity.QuestionBuy;
import com.sluv.server.domain.question.entity.QuestionFind;
import com.sluv.server.domain.question.entity.QuestionHowabout;
import com.sluv.server.domain.question.entity.QuestionImg;
import com.sluv.server.domain.question.entity.QuestionItem;
import com.sluv.server.domain.question.entity.QuestionLike;
import com.sluv.server.domain.question.entity.QuestionRecommend;
import com.sluv.server.domain.question.entity.QuestionRecommendCategory;
import com.sluv.server.domain.question.entity.QuestionReport;
import com.sluv.server.domain.question.entity.QuestionVote;
import com.sluv.server.domain.question.entity.RecentQuestion;
import com.sluv.server.domain.question.enums.QuestionStatus;
import com.sluv.server.domain.question.exception.QuestionNotFoundException;
import com.sluv.server.domain.question.exception.QuestionReportDuplicateException;
import com.sluv.server.domain.question.exception.QuestionTypeNotFoundException;
import com.sluv.server.domain.question.repository.QuestionImgRepository;
import com.sluv.server.domain.question.repository.QuestionItemRepository;
import com.sluv.server.domain.question.repository.QuestionLikeRepository;
import com.sluv.server.domain.question.repository.QuestionRecommendCategoryRepository;
import com.sluv.server.domain.question.repository.QuestionReportRepository;
import com.sluv.server.domain.question.repository.QuestionRepository;
import com.sluv.server.domain.question.repository.QuestionVoteRepository;
import com.sluv.server.domain.question.repository.RecentQuestionRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.cache.CacheService;
import com.sluv.server.global.common.response.PaginationResDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionImgRepository questionImgRepository;
    private final QuestionItemRepository questionItemRepository;
    private final QuestionRecommendCategoryRepository questionRecommendCategoryRepository;
    private final QuestionLikeRepository questionLikeRepository;
    private final QuestionReportRepository questionReportRepository;
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final CelebRepository celebRepository;
    private final NewCelebRepository newCelebRepository;
    private final RecentQuestionRepository recentQuestionRepository;
    private final ItemScrapRepository itemScrapRepository;
    private final ClosetRepository closetRepository;
    private final QuestionVoteRepository questionVoteRepository;
    private final UserRepository userRepository;

    private final CacheService cacheService;
    private final QuestionAlarmService questionAlarmService;


    @Transactional
    public QuestionPostResDto postQuestionFind(User user, QuestionFindPostReqDto dto) {
        /**
         * 1. 생성 or 수정
         * 2. QuestionFind 저장
         * 3. QuestionImg 저장
         * 4. QuestionItem 저장
         */
        log.info("찾아주세요 게시글 등록 or 수정 - 사용자 : {}, 질문 게시글 : {}, 질문 게시글 제목 : {}",
                user.getId(), dto.getId() == null ? null : dto.getId(), dto.getTitle());
        // 1. 생성 or 수정
        Celeb celeb = null;
        if (dto.getCelebId() != null) {
            celeb = celebRepository.findById(dto.getCelebId()).orElse(null);
        }
        NewCeleb newCeleb = null;
        if (dto.getNewCelebId() != null) {
            newCeleb = newCelebRepository.findById(dto.getNewCelebId()).orElse(null);
        }

        QuestionFind questionFind = QuestionFind.toEntity(user, dto, celeb, newCeleb);

        // 2. QuestionFind 저장
        QuestionFind newQuestionFind = questionRepository.save(questionFind);

        // 3. QuestionImg 저장
        postQuestionImgs(dto.getImgList(), newQuestionFind);

        // 4. QuestionItem 저장
        postQuestionItems(dto.getItemList(), newQuestionFind);

        return QuestionPostResDto.of(newQuestionFind.getId());

    }

    @Transactional
    public QuestionPostResDto postQuestionBuy(User user, QuestionBuyPostReqDto dto) {
        /**
         * 1. 생성 or 수정
         * 2. QuestionBuy 저장
         * 3. QuestionImg 저장
         * 4. QuestionItem 저장
         */
        log.info("이 중에 뭐 살까 게시글 등록 or 수정 - 사용자 : {}, 질문 게시글 : {}, 질문 게시글 제목 : {}",
                user.getId(), dto.getId() == null ? null : dto.getId(), dto.getTitle());

        // 1. 생성 or 수정
        QuestionBuy questionBuy = QuestionBuy.toEntity(user, dto);

        // 2. QuestionBuy 저장
        QuestionBuy newQuestionBuy = questionRepository.save(questionBuy);

        // 3. QuestionImg 저장
        postQuestionImgs(dto.getImgList(), newQuestionBuy);

        // 4. QuestionItem 저장
        postQuestionItems(dto.getItemList(), newQuestionBuy);

        return QuestionPostResDto.of(newQuestionBuy.getId());
    }

    @Transactional
    public QuestionPostResDto postQuestionHowabout(User user, QuestionHowaboutPostReqDto dto) {
        /**
         * 1. 생성 or 수정
         * 2. QuestionHowabout 저장
         * 3. QuestionImg 저장
         * 4. QuestionItem 저장
         */

        log.info("이거 어때 게시글 등록 or 수정 - 사용자 : {}, 질문 게시글 : {}, 질문 게시글 제목 : {}",
                user.getId(), dto.getId() == null ? null : dto.getId(), dto.getTitle());

        // 1. 생성 or 수정
        QuestionHowabout questionHowabout = QuestionHowabout.toEntity(user, dto);

        // 2. QuestionHotabout 저장
        QuestionHowabout newQuestionHowabout = questionRepository.save(questionHowabout);

        // 3. QuestionImg 저장
        postQuestionImgs(dto.getImgList(), newQuestionHowabout);

        // 4. QuestionItem 저장
        postQuestionItems(dto.getItemList(), newQuestionHowabout);

        return QuestionPostResDto.of(newQuestionHowabout.getId());
    }

    @Transactional
    public QuestionPostResDto postQuestionRecommend(User user, QuestionRecommendPostReqDto dto) {
        /**
         * 1. 생성 or 수정
         * 1. QuestionRecommend 저장
         * 2. Recommend Category 저장
         * 3. QuestionImg 저장
         * 4. QuestionItem 저장
         */
        log.info("추천해줘 게시글 등록 or 수정 - 사용자 : {}, 질문 게시글 : {}, 질문 게시글 제목 : {}",
                user.getId(), dto.getId() == null ? null : dto.getId(), dto.getTitle());
        // 1. 생성 or 수정
        QuestionRecommend questionRecommend = QuestionRecommend.toEntity(user, dto);

        // 2. QuestionRecommend 저장
        QuestionRecommend newQuestionRecommend = questionRepository.save(questionRecommend);

        // 3. Recommend Category 저장
        // Question에 대한 RecommendCategory 초기화
        questionRecommendCategoryRepository.deleteAllByQuestionId(newQuestionRecommend.getId());

        List<QuestionRecommendCategory> recommendCategoryList = dto.getCategoryNameList().stream()
                .map(categoryName ->
                        QuestionRecommendCategory.toEntity(newQuestionRecommend, categoryName)
                ).toList();

        questionRecommendCategoryRepository.saveAll(recommendCategoryList);

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
        questionImgRepository.deleteAllByQuestionId(question.getId());

        if (dtoList != null) {
            // Question Img들 추가
            List<QuestionImg> imgList = dtoList.stream()
                    .map(imgDto -> QuestionImg.toEntity(question, imgDto))
                    .toList();

            questionImgRepository.saveAll(imgList);
        }
    }

    /**
     * Questim Item 저장 메소드
     */
    private void postQuestionItems(List<QuestionItemReqDto> dtoList, Question question) {
        // Question에 대한 Item 초기화
        questionItemRepository.deleteAllByQuestionId(question.getId());
        if (dtoList != null) {
            // Question Item들 추가
            List<QuestionItem> itemList = dtoList.stream().map(itemDto -> {
                        Item item = itemRepository.findById(itemDto.getItemId()).orElseThrow(ItemNotFoundException::new);
                        return QuestionItem.toEntity(question, item, itemDto);
                    }
            ).toList();

            questionItemRepository.saveAll(itemList);
        }
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        log.info("질문 게시글 삭제 - 질문 게시글 : {}", questionId);
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);
        question.changeQuestionStatus(QuestionStatus.DELETED);
    }

    @Transactional
    public void postQuestionLike(User user, Long questionId) {
        log.info("질문 게시글 좋아요 - 사용자 : {}, 질문 게시글 : {}", user.getId(), questionId);
        // 해당 유저의 Question 게시물 like 여부 검색
        Boolean likeStatus = questionLikeRepository.existsByQuestionIdAndUserId(questionId, user.getId());
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

        if (likeStatus) {
            // like가 있다면 삭제
            questionLikeRepository.deleteByQuestionIdAndUserId(questionId, user.getId());
        } else {
            // like가 없다면 등록
            questionLikeRepository.save(QuestionLike.toEntity(user, question));
            questionAlarmService.sendAlarmAboutQuestionLike(user.getId(), question.getId());
        }

    }

    @Transactional
    public void postQuestionReport(User user, Long questionId, QuestionReportReqDto dto) {
        log.info("질문 게시글 신고 - 사용자 : {}, 질문 게시글 : {}, 사유 : {}", user.getId(), questionId, dto.getReason());
        Boolean reportExist = questionReportRepository.existsByQuestionIdAndReporterId(questionId, user.getId());

        if (!reportExist) {
            // 신고 내역이 없다면 신고 등록.
            Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

            questionReportRepository.save(
                    QuestionReport.toEntity(user, question, dto)
            );

        } else {
            // 신고 내역이 있다면 중복 신고 방지.
            throw new QuestionReportDuplicateException();
        }
    }

    @Transactional
    public QuestionGetDetailResDto getQuestionDetail(User nowUser, Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

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
        User writer = userRepository.findById(question.getUser().getId()).orElse(null);

        // Question img List
        List<QuestionImgResDto> questionImgList = questionImgRepository.findAllByQuestionId(questionId).
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

        List<Closet> closetList;

        if (nowUser != null) {
            closetList = closetRepository.findAllByUserId(nowUser.getId());
        } else {
            closetList = new ArrayList<>();
        }

        // Question Item List
        List<QuestionItemResDto> questionItemList = questionItemRepository.findAllByQuestionId(questionId)
                .stream()
                .map(questionItem -> {
                    ItemSimpleResDto itemSimpleResDto = ItemSimpleResDto.of(
                            questionItem.getItem(),
                            itemImgRepository.findMainImg(questionItem.getItem().getId()),
                            itemScrapRepository.getItemScrapStatus(questionItem.getItem(), closetList)
                    );
                    QuestionVoteDataDto questionVoteDataDto = null;
                    // QuestionBuy일 경우 투표수 추가.
                    if (qType != null & qType.equals("Buy")) {
                        questionVoteDataDto = getVoteData(questionId, (long) questionItem.getSortOrder());
                    }

                    return QuestionItemResDto.of(questionItem, itemSimpleResDto, questionVoteDataDto);
                }).toList();

        // Question Like Num Count
        Long questionLikeNum = questionLikeRepository.countByQuestionId(questionId);

        // Question Comment Num Count
        Long questionCommentNum = commentRepository.countByQuestionId(questionId);

        // hasLike 검색
        Boolean currentUserLike =
                nowUser != null && questionLikeRepository.existsByQuestionIdAndUserId(questionId, nowUser.getId());

        CelebChipResDto celeb = null;
        CelebChipResDto newCeleb = null;
        LocalDateTime voteEndTime = null;
        Long totalVoteNum = null;
        Long voteStatus = null;
        List<String> recommendCategoryList = null;

        switch (qType) {
            case "Find" -> {
                QuestionFind questionFind = (QuestionFind) question;
                if (questionFind.getCeleb() != null) {
                    celeb = CelebChipResDto.of(questionFind.getCeleb());
                } else {
                    newCeleb = CelebChipResDto.of(questionFind.getNewCeleb());
                }
            }
            case "Buy" -> {
                QuestionBuy questionBuy = (QuestionBuy) question;
                QuestionVote questionVote =
                        nowUser != null ?
                                questionVoteRepository.findByQuestionIdAndUserId(questionId, nowUser.getId())
                                        .orElse(null)
                                : null;
                voteEndTime = questionBuy.getVoteEndTime();
                totalVoteNum = questionVoteRepository.countByQuestionId(questionId);
                voteStatus = questionVote != null
                        ? questionVote.getVoteSortOrder()
                        : null;
            }
            case "Recommend" ->
                    recommendCategoryList = questionRecommendCategoryRepository.findAllByQuestionId(questionId)
                            .stream().map(QuestionRecommendCategory::getName).toList();
        }

        // RecentQuestion 등록
        if (nowUser != null) {
            recentQuestionRepository.save(RecentQuestion.toEntity(nowUser, qType, question));

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
        boolean isExist = cacheService.existUserViewQuestionId(userId, question.getId());
        if (!isExist) {
            cacheService.saveUserViewQuestionId(userId, question.getId());
            question.increaseSearchNum();
        }
    }

    /**
     * builder에 VoteNum, VotePercent 탑재
     */
    private QuestionVoteDataDto getVoteData(Long questionId, Long sortOrder) {
        List<QuestionVote> questionVotes = questionVoteRepository.findAllByQuestionId(questionId);

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
    public void postQuestionVote(User user, Long questionId, QuestionVoteReqDto dto) {
        log.info("질문 게시글 투표 - 사용자 : {}, 질문 게시글 : {}, 투표 : {}", user.getId(), questionId, dto.getVoteSortOrder());
        QuestionVote questionVote = questionVoteRepository.findByQuestionIdAndUserId(questionId, user.getId())
                .orElse(null);

        if (questionVote == null) {
            // 투표 등록

            // Question 검색
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(QuestionNotFoundException::new);

            // QuestionVote 생성 및 저장
            questionVoteRepository.save(
                    QuestionVote.toEntity(question, user, dto)
            );

        } else {
            // 투표 취소

            // 해당 QuestionVote 삭제.
            questionVoteRepository.deleteById(questionVote.getId());
        }
    }

    /**
     * Question 상세보기 하단의 추천 게시글
     * TODO 게시글과 같은 셀럽/같은 그룹 로직 추가해야함 (23.06.22)
     */
    @Transactional(readOnly = true)
    public List<QuestionSimpleResDto> getWaitQuestionBuy(User user, Long questionId) {

        List<Celeb> interestedCeleb = new ArrayList<>();
        if (user != null) {
            interestedCeleb = celebRepository.findInterestedCeleb(user);
        }

        return questionRepository.getWaitQuestionBuy(user, questionId, interestedCeleb)
                .stream()
                .map(questionBuy -> getQuestionSimpleResDto(questionBuy, "Buy"))
                .toList();
    }

    /**
     * Wait QuestionRecommend 조회
     */
    @Transactional(readOnly = true)
    public List<QuestionSimpleResDto> getWaitQuestionRecommend(User user, Long questionId) {
        return questionRepository.getWaitQuestionRecommend(user, questionId)
                .stream()
                .map(questionRecommend -> getQuestionSimpleResDto(questionRecommend, "Recommend"))
                .toList();
    }

    /**
     * Wait QuestionHowabout 조회
     */
    @Transactional(readOnly = true)
    public List<QuestionSimpleResDto> getWaitQuestionHowabout(User user, Long questionId) {
        return questionRepository.getWaitQuestionHowabout(user, questionId)
                .stream()
                .map(questionHowabout -> getQuestionSimpleResDto(questionHowabout, "How"))
                .toList();
    }

    /**
     * Wait QuestionFind 조회
     */
    @Transactional(readOnly = true)
    public List<QuestionSimpleResDto> getWaitQuestionFind(User user, Long questionId) {
        List<Celeb> interestedCeleb = new ArrayList<>();

        if (user != null) {
            interestedCeleb = celebRepository.findInterestedCeleb(user);
        }

        return questionRepository.getWaitQuestionFind(user, questionId, interestedCeleb)
                .stream()
                .map(questionFind -> getQuestionSimpleResDto(questionFind, "Find"))
                .toList();
    }

    public QuestionSimpleResDto getQuestionSimpleResDto(Question question, String qType) {
        log.warn("아악2");

        List<QuestionImgSimpleResDto> imgList = new ArrayList<>();
        List<QuestionImgSimpleResDto> itemImgList = new ArrayList<>();
        String celebName = null;
        List<String> categoryNameList = null;

//        if (!qType.equals("Buy")) {
        if (!(question instanceof QuestionBuy)) {
            // 이미지 URL
            QuestionImg questionImg = questionImgRepository.findByQuestionIdAndRepresentFlag(question.getId(), true);

            // 이미지 Dto로 변경
            if (questionImg != null) {
                imgList.add(QuestionImgSimpleResDto.of(questionImg));
            }

            // 아이템 이미지 URL
            QuestionItem questionItem = questionItemRepository.findByQuestionIdAndRepresentFlag(question.getId(), true);

            // 아이템 이미지 Dto로 변경
            if (questionItem != null) {
                ItemImg mainImg = itemImgRepository.findMainImg(questionItem.getItem().getId());
                imgList.add(QuestionImgSimpleResDto.of(mainImg));
            }

        } else {
            // 이미지 URL
            imgList = questionImgRepository.findAllByQuestionId(question.getId())
                    .stream()
                    .map(QuestionImgSimpleResDto::of).toList();

            // 아이템 이미지 URL
            itemImgList = questionItemRepository.findAllByQuestionId(question.getId())
                    .stream()
                    .map(item -> {
                        ItemImg mainImg = itemImgRepository.findMainImg(item.getItem().getId());
                        return QuestionImgSimpleResDto.of(mainImg);
                    }).toList();
        }

//        if (qType.equals("Recommend")) {
        if (question instanceof QuestionRecommend) {
            categoryNameList = questionRecommendCategoryRepository.findAllByQuestionId(question.getId()).stream()
                    .map(QuestionRecommendCategory::getName).toList();
        }

        // Question 좋아요 수
        Long likeNum = questionLikeRepository.countByQuestionId(question.getId());

        // Question 댓글 수
        Long commentNum = commentRepository.countByQuestionId(question.getId());

        User writer = userRepository.findById(question.getUser().getId()).orElse(null);

        return QuestionSimpleResDto.of(question, writer, likeNum, commentNum,
                imgList, itemImgList, categoryNameList);
    }

    /**
     * Question 리스트를 최신순으로 조회
     */
    @Transactional(readOnly = true)
    public PaginationResDto<QuestionSimpleResDto> getTotalQuestionList(Pageable pageable) {
        Page<Question> questionPage = questionRepository.getTotalQuestionList(pageable);
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

        return PaginationResDto.of(questionPage, content);
    }

    @Transactional(readOnly = true)
    public PaginationResDto<QuestionBuySimpleResDto> getQuestionBuyList(User user, String voteStatus,
                                                                        Pageable pageable) {
        Page<QuestionBuy> questionPage = questionRepository.getQuestionBuyList(voteStatus, pageable);

        List<QuestionBuySimpleResDto> content = questionPage.stream().map(question -> {

            List<QuestionImgResDto> imgList = questionImgRepository.findAllByQuestionId(question.getId())
                    .stream()
                    .map(questionImg -> {
                        QuestionVoteDataDto voteDataDto = getVoteData(questionImg.getQuestion().getId(),
                                (long) questionImg.getSortOrder());

                        return QuestionImgResDto.of(questionImg, voteDataDto);
                    }).toList();

            // 아이템 이미지 URL
            List<QuestionItemResDto> itemImgList = questionItemRepository.findAllByQuestionId(question.getId())
                    .stream()
                    .map(questionItem -> {
                        ItemSimpleResDto itemSimpleResDto = ItemSimpleResDto.of(
                                questionItem.getItem(),
                                itemImgRepository.findMainImg(questionItem.getItem().getId()),
                                null
                        );
                        QuestionVoteDataDto questionVoteDataDto = getVoteData(questionItem.getQuestion().getId(),
                                (long) questionItem.getSortOrder());
                        return QuestionItemResDto.of(questionItem, itemSimpleResDto, questionVoteDataDto);
                    }).toList();

            Long voteCount = getTotalVoteCount(imgList, itemImgList);

            QuestionVote questionVote = questionVoteRepository.findByQuestionIdAndUserId(question.getId(), user.getId())
                    .orElse(null);

            User writer = userRepository.findById(question.getUser().getId())
                    .orElse(null);

            return QuestionBuySimpleResDto.of(user, question, writer, voteCount, imgList, itemImgList,
                    question.getVoteEndTime(),
                    questionVote);
        }).toList();

        return PaginationResDto.of(questionPage, content);
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
    public PaginationResDto<QuestionSimpleResDto> getQuestionFindList(Long celebId, Pageable pageable) {

        Page<QuestionFind> questionPage = questionRepository.getQuestionFindList(celebId, pageable);

        List<QuestionSimpleResDto> content = questionPage.stream().map(question ->
                getQuestionSimpleResDto(question, "Find")
        ).toList();

        return PaginationResDto.of(questionPage, content);
    }

    @Transactional(readOnly = true)
    public PaginationResDto<QuestionSimpleResDto> getQuestionHowaboutList(Pageable pageable) {
        Page<QuestionHowabout> questionPage = questionRepository.getQuestionHowaboutList(pageable);
        List<QuestionSimpleResDto> content = questionPage.stream().map(question ->
                getQuestionSimpleResDto(question, "How")
        ).toList();

        return PaginationResDto.of(questionPage, content);
    }

    @Transactional(readOnly = true)
    public PaginationResDto<QuestionSimpleResDto> getQuestionRecommendList(String hashtag, Pageable pageable) {
        Page<QuestionRecommend> questionPage = questionRepository.getQuestionRecommendList(hashtag, pageable);
        List<QuestionSimpleResDto> content = questionPage.stream().map(question ->
                getQuestionSimpleResDto(question, "Recommend")
        ).toList();

        return PaginationResDto.of(questionPage, content);
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
        List<Question> dailyHoyQuestionList = questionRepository.getDailyHotQuestion();

        List<QuestionHomeResDto> result = dailyHoyQuestionList.stream().map(question -> {
            List<QuestionImgSimpleResDto> questionImgSimpleList = getQuestionImgSimpleList(question);
            User writer = userRepository.findById(question.getUser().getId()).orElse(null);
            return QuestionHomeResDto.of(question, writer, questionImgSimpleList);

        }).toList();

        return result;

    }

    private List<QuestionImgSimpleResDto> getQuestionImgSimpleList(Question question) {

        // Question이 QuestionBuy인 경우 모든 이미지를 순서대로 조회
        if (question instanceof QuestionBuy) {
            List<QuestionImgSimpleResDto> result = new ArrayList<>();

            List<QuestionImg> questionImgList = questionImgRepository.findAllByQuestionId(question.getId());
            List<ItemImg> questionItemImgList = questionItemRepository.findAllByQuestionId(question.getId()).stream()
                    .map(questionItem -> itemImgRepository.findMainImg(questionItem.getItem().getId())).toList();

            questionImgList.forEach(questionImg -> result.add(QuestionImgSimpleResDto.of(questionImg)));
            questionItemImgList.forEach(itemImg -> result.add(QuestionImgSimpleResDto.of(itemImg)));

            result.sort(Comparator.comparing(QuestionImgSimpleResDto::getSortOrder));

            return result;
        } else {// 그 외에는 대표이미지만 조화

            QuestionImg questionImg = questionImgRepository.findByQuestionIdAndRepresentFlag(question.getId(), true);
            QuestionItem questionItem = questionItemRepository.findByQuestionIdAndRepresentFlag(question.getId(), true);

            String imgUrl = null;

            if (questionImg != null) {
                imgUrl = questionImg.getImgUrl();
            } else if (questionItem != null) {
                imgUrl = itemImgRepository.findMainImg(questionItem.getItem().getId()).getItemImgUrl();
            }

            return imgUrl != null
                    ? Arrays.asList(new QuestionImgSimpleResDto(imgUrl, 0L))
                    : null;
        }
    }

    /**
     * 주간 Hot Question 조회 기능.
     */
    @Transactional(readOnly = true)
    public PaginationResDto<QuestionSimpleResDto> getWeeklyHotQuestionList(Pageable pageable) {
        Page<Question> page = questionRepository.getWeeklyHotQuestion(pageable);

        List<QuestionSimpleResDto> content = page.stream().map(question -> {
            List<String> categoryList = null;
            if (question instanceof QuestionRecommend) {
                categoryList = questionRecommendCategoryRepository.findAllByQuestionId(question.getId())
                        .stream()
                        .map(QuestionRecommendCategory::getName).toList();
            }

            List<QuestionImgSimpleResDto> imgList = questionImgRepository.findAllByQuestionId(question.getId())
                    .stream()
                    .map(QuestionImgSimpleResDto::of)
                    .toList();

            List<QuestionImgSimpleResDto> itemImgList = questionItemRepository.findAllByQuestionId(question.getId())
                    .stream()
                    .map(questionItem ->
                            QuestionImgSimpleResDto.of(itemImgRepository.findMainImg(questionItem.getItem().getId()))
                    )
                    .toList();

            Long commentNum = commentRepository.countByQuestionId(question.getId());
            Long likeNum = questionLikeRepository.countByQuestionId(question.getId());
            User writer = userRepository.findById(question.getUser().getId()).orElse(null);

            return QuestionSimpleResDto.of(question, writer, likeNum, commentNum, imgList, itemImgList, categoryList);
        }).toList();

        return PaginationResDto.of(page, content);
    }
}

