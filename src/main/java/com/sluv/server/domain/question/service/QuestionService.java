package com.sluv.server.domain.question.service;

import com.sluv.server.domain.celeb.dto.CelebChipResDto;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.celeb.repository.NewCelebRepository;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.repository.ClosetRepository;
import com.sluv.server.domain.comment.repository.CommentRepository;
import com.sluv.server.domain.item.entity.ItemImg;
import com.sluv.server.domain.item.repository.ItemScrapRepository;
import com.sluv.server.domain.question.dto.QuestionItemResDto;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.exception.ItemNotFoundException;
import com.sluv.server.domain.item.repository.ItemImgRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.question.dto.*;
import com.sluv.server.domain.question.entity.*;
import com.sluv.server.domain.question.enums.QuestionStatus;
import com.sluv.server.domain.question.exception.QuestionNotFoundException;
import com.sluv.server.domain.question.exception.QuestionReportDuplicateException;
import com.sluv.server.domain.question.exception.QuestionTypeNotFoundException;
import com.sluv.server.domain.question.repository.*;
import com.sluv.server.domain.user.dto.UserInfoDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.enums.ItemImgOrLinkStatus;
import com.sluv.server.global.common.enums.ReportStatus;
import com.sluv.server.global.common.response.PaginationResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
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

    @Transactional
    public QuestionPostResDto postQuestionFind(User user, QuestionFindPostReqDto dto) {
        /**
         * 1. 생성 or 수정
         * 2. QuestionFind 저장
         * 3. QuestionImg 저장
         * 4. QuestionItem 저장
         */
        // 1. 생성 or 수정
        Celeb celeb = null;
        if(dto.getCelebId() != null) {
           celeb = celebRepository.findById(dto.getCelebId()).orElse(null);
        }
        NewCeleb newCeleb = null;
        if(dto.getNewCelebId() != null) {
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
    private void postQuestionImgs(List<QuestionImgReqDto> dtoList, Question question){
        // Question에 대한 Img 초기화
        questionImgRepository.deleteAllByQuestionId(question.getId());

        if(dtoList != null) {
            // Question Img들 추가
            List<QuestionImg> imgList = dtoList.stream()
                    .map(imgDto -> QuestionImg.toEntity(question, imgDto))
                    .toList();

            questionImgRepository.saveAll(imgList);
        }
    }

    /**
     * Questim Item 저장 메소드
     *
     */
    private void postQuestionItems(List<QuestionItemReqDto> dtoList, Question question){
        // Question에 대한 Item 초기화
        questionItemRepository.deleteAllByQuestionId(question.getId());
        if(dtoList != null) {
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
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

        question.changeQuestionStatus(QuestionStatus.DELETED);
    }

    @Transactional
    public void postQuestionLike(User user, Long questionId) {
        // 해당 유저의 Question 게시물 like 여부 검색
        Boolean likeStatus = questionLikeRepository.existsByQuestionIdAndUserId(questionId, user.getId());
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

        if(likeStatus) {
            // like가 있다면 삭제
            questionLikeRepository.deleteByQuestionIdAndUserId(questionId, user.getId());
        }else {
            // like가 없다면 등록

            questionLikeRepository.save(
                    QuestionLike.toEntity(user, question)
            );
        }
        question.decreaseSearchNum();

    }

    public void postQuestionReport(User user, Long questionId, QuestionReportReqDto dto) {
        Boolean reportExist = questionReportRepository.existsByQuestionIdAndReporterId(questionId, user.getId());

        if(!reportExist) {
            // 신고 내역이 없다면 신고 등록.
            Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

            questionReportRepository.save(
                    QuestionReport.toEntity(user, question, dto)
            );

        }else{
            // 신고 내역이 있다면 중복 신고 방지.
            throw new QuestionReportDuplicateException();
        }
    }

    @Transactional
    public QuestionGetDetailResDto getQuestionDetail(User user, Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

        // Question Type 분류
        String qType;
        if(question != null){
            if (question instanceof QuestionFind){
                qType = "Find";
            }else if(question instanceof QuestionBuy){
                qType = "Buy";
            }else if(question instanceof QuestionHowabout){
                qType = "How";
            }else if(question instanceof QuestionRecommend){
                qType = "Recommend";
            } else {
                qType = null;
            }
        } else {
            qType = null;
        }

        // 작성자
        User writer = question.getUser();

        // Question img List
        List<QuestionImgResDto> questionImgList= questionImgRepository.findAllByQuestionId(questionId).
                                                            stream()
                                                            .map(questionImg -> {
                                                                QuestionVoteDataDto voteDataDto = null;
                                                                // QuestionBuy 라면
                                                                if(qType != null && qType.equals("Buy")){
                                                                    voteDataDto = getVoteData(questionId, (long) questionImg.getSortOrder());
                                                                }

                                                                return  QuestionImgResDto.of(questionImg, voteDataDto);
                                                            }
                                                            ).toList();

        // Question Item List
        List<QuestionItemResDto> questionItemList = questionItemRepository.findAllByQuestionId(questionId)
                                            .stream()
                                            .map(questionItem -> {
                                                List<Closet> closetList = closetRepository.findAllByUserId(user.getId());
                                                ItemSimpleResDto itemSimpleResDto = ItemSimpleResDto.of(
                                                                        questionItem.getItem(),
                                                                        itemImgRepository.findMainImg(questionItem.getItem().getId()),
                                                                        itemScrapRepository.getItemScrapStatus(questionItem.getItem(),closetList)
                                                                );
                                                QuestionVoteDataDto questionVoteDataDto = null;
                                                // QuestionBuy일 경우 투표수 추가.
                                                if(qType != null & qType.equals("Buy")){
                                                    questionVoteDataDto = getVoteData(questionId, (long) questionItem.getSortOrder());
                                                }

                                                return QuestionItemResDto.of(questionItem, itemSimpleResDto, questionVoteDataDto);
                                            }).toList();

        // Question Like Num Count
        Long questionLikeNum = questionLikeRepository.countByQuestionId(questionId);

        // Question Comment Num Count
        Long questionCommentNum = commentRepository.countByQuestionId(questionId);

        // hasLike 검색
        Boolean currentUserLike = questionLikeRepository.existsByQuestionIdAndUserId(questionId, user.getId());

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
                QuestionVote questionVote = questionVoteRepository.findByQuestionIdAndUserId(questionId, user.getId())
                        .orElse(null);
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
        recentQuestionRepository.save(
                RecentQuestion.toEntity(user, qType, question)
        );

        // SearchNum 증가
        question.increaseSearchNum();

        return QuestionGetDetailResDto.of(
                question, qType, writer,
                questionImgList, questionItemList,
                questionLikeNum, questionCommentNum, currentUserLike,
                user.getId().equals(writer.getId()),
                celeb, newCeleb,
                voteEndTime, totalVoteNum, voteStatus,
                recommendCategoryList
        );
    }

    /**
     * builder에 VoteNum, VotePercent 탑재
     */
    private QuestionVoteDataDto getVoteData(Long questionId, Long sortOrder) {


        // 해당 SortOrder의 투표 수
        Long voteNum = questionVoteRepository.countByQuestionIdAndVoteSortOrder(questionId, sortOrder);
        // 전체 투표 수
        Long totalVoteNum = questionVoteRepository.countByQuestionId(questionId);

        return QuestionVoteDataDto.of(
                    voteNum,
                    totalVoteNum != 0
                    ? getVotePercent(voteNum, totalVoteNum)
                    : 0
                );
    }

    /**
     * QuestionVote 퍼센트 계산.
     */
    private Double getVotePercent(Long voteNum, Long totalVoteNum){
        double div = (double) voteNum / (double) totalVoteNum;

        return Math.round( div * 1000 ) / 10.0;
    }

    /**
     * QuestionBuy 등록 및 취소
     */
    public void postQuestionVote(User user, Long questionId, QuestionVoteReqDto dto) {
        QuestionVote questionVote = questionVoteRepository.findByQuestionIdAndUserId(questionId, user.getId()).orElse(null);

        if(questionVote == null){
            // 투표 등록

            // Question 검색
            Question question = questionRepository.findById(questionId)
                                                    .orElseThrow(QuestionNotFoundException::new);

            // QuestionVote 생성 및 저장
            questionVoteRepository.save(
                    QuestionVote.toEntity(question, user, dto)
            );

        }else{
            // 투표 취소

            // 해당 QuestionVote 삭제.
            questionVoteRepository.deleteById(questionVote.getId());
        }
    }

    /**
     * Question 상세보기 하단의 추천 게시글
     * TODO 게시글과 같은 셀럽/같은 그룹 로직 추가해야함 (23.06.22)
     */

    public List<QuestionSimpleResDto> getWaitQuestionBuy(User user, Long questionId) {
        List<Celeb> interestedCeleb = celebRepository.findInterestedCeleb(user);

        return questionRepository.getWaitQuestionBuy(user, questionId, interestedCeleb)
                .stream()
                .map(questionBuy ->
                    getQuestionSimpleResDto(questionBuy,
                            "Buy"
                    )
                ).toList();
    }

    /**
     * Wait QuestionRecommend 조회
     */

    public List<QuestionSimpleResDto> getWaitQuestionRecommend(User user, Long questionId) {
        return questionRepository.getWaitQuestionRecommend(user, questionId)
                .stream()
                .map(questionRecommend ->
                    getQuestionSimpleResDto(questionRecommend,
                            "Recommend"
                    )
                ).toList();
    }

    /**
     * Wait QuestionHowabout 조회
     */

    public List<QuestionSimpleResDto> getWaitQuestionHowabout(User user, Long questionId) {
        return questionRepository.getWaitQuestionHowabout(user, questionId)
                .stream()
                .map(questionHowabout ->
                    getQuestionSimpleResDto(questionHowabout,
                            "How"
                    )
                ).toList();
    }

    /**
     * Wait QuestionFind 조회
     */

    public List<QuestionSimpleResDto> getWaitQuestionFind(User user, Long questionId) {
        List<Celeb> interestedCeleb = celebRepository.findInterestedCeleb(user);

        return questionRepository.getWaitQuestionFind(user, questionId, interestedCeleb)
                .stream()
                .map(questionFind ->
                        getQuestionSimpleResDto(questionFind,
                                "Find"
                                )
                  ).toList();
    }

//    private QuestionSimpleResDto getQuestionSimpleResDto(Long questionId, String qType, String title, String content) {
    private QuestionSimpleResDto getQuestionSimpleResDto(Question question, String qType) {

        List<QuestionImgSimpleResDto> imgList = new ArrayList<>();
        List<QuestionImgSimpleResDto> itemImgList = new ArrayList<>();
        String celebName = null;
        List<String> categoryNameList = null;


        if(!qType.equals("Buy")) {
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

        }else{
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

        if(qType.equals("Recommend")){
            categoryNameList = questionRecommendCategoryRepository.findAllByQuestionId(question.getId()).stream()
                    .map(QuestionRecommendCategory::getName).toList();
        }else if(qType.equals("Find")){
            QuestionFind questionFind = (QuestionFind) question;

            celebName = getQuestionCelebName(questionFind);
        }
        // 작성자 InfoDto
        UserInfoDto userInfoDto = UserInfoDto.of(question.getUser());

        // Question 좋아요 수
        Long likeNum = questionLikeRepository.countByQuestionId(question.getId());

        // Question 댓글 수
        Long commentNum = commentRepository.countByQuestionId(question.getId());



        return QuestionSimpleResDto.of(qType, userInfoDto, likeNum, commentNum,
                question, celebName, imgList, itemImgList, categoryNameList);
    }

    /**
     * Question 리스트를 최신순으로 조회
     */
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

        return PaginationResDto.<QuestionSimpleResDto>builder()
                .page(questionPage.getNumber())
                .hasNext(questionPage.hasNext())
                .content(content)
                .build();
    }

    public PaginationResDto<QuestionSimpleResDto> getQuestionBuyList(Pageable pageable) {
        Page<QuestionBuy> questionPage = questionRepository.getQuestionBuyList(pageable);
        List<QuestionSimpleResDto> content = questionPage.stream().map(question ->
            getQuestionSimpleResDto(question, "Buy")
        ).toList();

        return PaginationResDto.<QuestionSimpleResDto>builder()
                .page(questionPage.getNumber())
                .hasNext(questionPage.hasNext())
                .content(content)
                .build();
    }

    public PaginationResDto<QuestionSimpleResDto> getQuestionFindList(Pageable pageable) {
        Page<QuestionFind> questionPage = questionRepository.getQuestionFindList(pageable);
        List<QuestionSimpleResDto> content = questionPage.stream().map(question ->
            getQuestionSimpleResDto(question, "Find")
        ).toList();

        return PaginationResDto.<QuestionSimpleResDto>builder()
                .page(questionPage.getNumber())
                .hasNext(questionPage.hasNext())
                .content(content)
                .build();
    }

    public PaginationResDto<QuestionSimpleResDto> getQuestionHowaboutList(Pageable pageable) {
        Page<QuestionHowabout> questionPage = questionRepository.getQuestionHowaboutList(pageable);
        List<QuestionSimpleResDto> content = questionPage.stream().map(question ->
                getQuestionSimpleResDto(question, "How")
        ).toList();

        return PaginationResDto.<QuestionSimpleResDto>builder()
                .page(questionPage.getNumber())
                .hasNext(questionPage.hasNext())
                .content(content)
                .build();
    }


    public PaginationResDto<QuestionSimpleResDto> getQuestionRecommendList(Pageable pageable) {
        Page<QuestionRecommend> questionPage = questionRepository.getQuestionRecommendList(pageable);
        List<QuestionSimpleResDto> content = questionPage.stream().map(question ->
                getQuestionSimpleResDto(question, "Recommend")
        ).toList();

        return PaginationResDto.<QuestionSimpleResDto>builder()
                .page(questionPage.getNumber())
                .hasNext(questionPage.hasNext())
                .content(content)
                .build();
    }

    private String getQuestionCelebName(QuestionFind questionFind){
        return questionFind.getCeleb() != null
                    ?questionFind.getCeleb().getParent() != null
                            ?questionFind.getCeleb().getParent().getCelebNameKr() + " " + questionFind.getCeleb().getCelebNameKr()
                            :questionFind.getCeleb().getCelebNameKr()
                    : questionFind.getNewCeleb().getCelebName();
    }
}

