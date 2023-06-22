package com.sluv.server.domain.question.service;

import com.sluv.server.domain.celeb.dto.CelebChipResDto;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.celeb.repository.NewCelebRepository;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.repository.ClosetRepository;
import com.sluv.server.domain.comment.repository.CommentRepository;
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
import com.sluv.server.domain.question.repository.*;
import com.sluv.server.domain.user.dto.UserInfoDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.enums.ItemImgOrLinkStatus;
import com.sluv.server.global.common.enums.ReportStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

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
        QuestionFind.QuestionFindBuilder questionFindBuilder = QuestionFind.builder();
        if(dto.getId() != null){
            questionFindBuilder.id(dto.getId());
        }

        // 2. QuestionFind 저장
        Celeb celeb = celebRepository.findById(dto.getCelebId()).orElse(null);
        NewCeleb newCeleb = newCelebRepository.findById(dto.getNewCelebId()).orElse(null);

        QuestionFind questionFind = questionFindBuilder
                .user(user)
                .title(dto.getTitle())
                .content(dto.getContent())
                .searchNum(0L)
                .celeb(celeb)
                .newCeleb(newCeleb)
                .questionStatus(QuestionStatus.ACTIVE)
                .build();

        QuestionFind newQuestionFind = questionRepository.save(questionFind);

        // 3. QuestionImg 저장
        postQuestionImgs(dto.getImgList().stream(), questionFind);


        // 4. QuestionItem 저장
        postQuestionItems(dto.getItemList().stream(), questionFind);

        return QuestionPostResDto.builder()
                .id(newQuestionFind.getId())
                .build();

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
        QuestionBuy.QuestionBuyBuilder questionBuyBuilder = QuestionBuy.builder();
        if(dto.getId() != null){
            questionBuyBuilder.id(dto.getId());
        }

        // 2. QuestionBuy 저장
        QuestionBuy questionBuy = questionBuyBuilder
                .user(user)
                .title(dto.getTitle())
                .searchNum(0L)
                .voteEndTime(dto.getVoteEndTime())
                .questionStatus(QuestionStatus.ACTIVE)
                .build();

        QuestionBuy newQuestionBuy = questionRepository.save(questionBuy);

        // 3. QuestionImg 저장
        postQuestionImgs(dto.getImgList().stream(), questionBuy);

        // 4. QuestionItem 저장
        postQuestionItems(dto.getItemList().stream(), questionBuy);

        return QuestionPostResDto.builder()
                .id(newQuestionBuy.getId())
                .build();
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
        QuestionHowabout.QuestionHowaboutBuilder questionHowaboutBuilder = QuestionHowabout.builder();
        if(dto.getId() != null){
            questionHowaboutBuilder.id(dto.getId());
        }

        // 2. QuestionHotabout 저장
        QuestionHowabout questionHowabout = questionHowaboutBuilder
                .user(user)
                .title(dto.getTitle())
                .content(dto.getContent())
                .searchNum(0L)
                .questionStatus(QuestionStatus.ACTIVE)
                .build();

        QuestionHowabout newQuestionHowabout = questionRepository.save(questionHowabout);

        // 3. QuestionImg 저장
        postQuestionImgs(dto.getImgList().stream(), newQuestionHowabout);

        // 4. QuestionItem 저장
        postQuestionItems(dto.getItemList().stream(), newQuestionHowabout);

        return QuestionPostResDto.builder()
                .id(newQuestionHowabout.getId())
                .build();
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
        QuestionRecommend.QuestionRecommendBuilder questionRecommendBuilder = QuestionRecommend.builder();
        if(dto.getId() != null){
            questionRecommendBuilder.id(dto.getId());
        }

        // 2. QuestionRecommend 저장
        QuestionRecommend questionRecommend = questionRecommendBuilder
                .user(user)
                .title(dto.getTitle())
                .content(dto.getContent())
                .searchNum(0L)
                .questionStatus(QuestionStatus.ACTIVE)
                .build();

        QuestionRecommend newQuestionRecommend = questionRepository.save(questionRecommend);

        // 3. Recommend Category 저장
        // Question에 대한 RecommendCategory 초기화
        questionRecommendCategoryRepository.deleteAllByQuestionId(newQuestionRecommend.getId());

        List<QuestionRecommendCategory> recommendCategoryList = dto.getCategoryNameList().stream()
                        .map(categoryName ->
                                QuestionRecommendCategory.builder()
                                .question(newQuestionRecommend)
                                .name(categoryName)
                                .build()
                        ).toList();

        questionRecommendCategoryRepository.saveAll(recommendCategoryList);

        // 4. QuestionImg 저장
        postQuestionImgs(dto.getImgList().stream(), newQuestionRecommend);

        // 4. QuestionItem 저장
        postQuestionItems(dto.getItemList().stream(), newQuestionRecommend);

        return QuestionPostResDto.builder()
                .id(newQuestionRecommend.getId())
                .build();
    }

    /**
     * Question Img 저장 메소드
     * @param dtoStream
     * @param question
     */
    private void postQuestionImgs(Stream<QuestionImgReqDto> dtoStream, Question question){
        // Question에 대한 Img 초기화
        questionImgRepository.deleteAllByQuestionId(question.getId());

        // Question Img들 추가
        List<QuestionImg> imgList = dtoStream.map(imgDto -> QuestionImg.builder()
                .question(question)
                .imgUrl(imgDto.getImgUrl())
                .description(imgDto.getDescription())
                .representFlag(imgDto.getRepresentFlag())
                .itemImgOrLinkStatus(ItemImgOrLinkStatus.ACTIVE)
                .sortOrder(imgDto.getSortOrder())
                .build()
        ).toList();

        questionImgRepository.saveAll(imgList);
    }

    /**
     * Questim Item 저장 메소드
     * @param dtoStream
     * @param question
     */
    private void postQuestionItems(Stream<QuestionItemReqDto> dtoStream, Question question){
        // Question에 대한 Item 초기화
        questionItemRepository.deleteAllByQuestionId(question.getId());

        // Question Item들 추가
        List<QuestionItem> itemList = dtoStream.map(itemDto -> {
                    Item item = itemRepository.findById(itemDto.getItemId()).orElseThrow(ItemNotFoundException::new);
                    return QuestionItem.builder()
                            .question(question)
                            .item(item)
                            .description(itemDto.getDescription())
                            .representFlag(itemDto.getRepresentFlag())
                            .sortOrder(itemDto.getSortOrder())
                            .build();
                }
        ).toList();

        questionItemRepository.saveAll(itemList);
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
                    QuestionLike.builder()
                            .user(user)
                            .question(question)
                            .build()
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
                    QuestionReport.builder()
                            .reporter(user)
                            .question(question)
                            .questionReportReason(dto.getReason())
                            .content(dto.getContent())
                            .reportStatus(ReportStatus.WAITING)
                            .build()
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
                                                                QuestionImgResDto.QuestionImgResDtoBuilder builder = QuestionImgResDto.builder()
                                                                                .imgUrl(questionImg.getImgUrl())
                                                                                .representFlag(questionImg.getRepresentFlag())
                                                                                .sortOrder(questionImg.getSortOrder())
                                                                                .description(questionImg.getDescription());

                                                                // QuestionBuy 라면
                                                                if(qType != null && qType.equals("Buy")){
                                                                    addVoteNum(questionId, (long) questionImg.getSortOrder(), builder, null);
                                                                }
                                                                    return  builder.build();
                                                            }
                                                            ).toList();

        // Question Item List
        List<QuestionItemResDto> questionItemList = questionItemRepository.findAllByQuestionId(questionId)
                                            .stream()
                                            .map(questionItem -> {
                                                List<Closet> closetList = closetRepository.findAllByUserId(user.getId());
                                                ItemSimpleResDto dto = ItemSimpleResDto.builder()
                                                                .itemId(questionItem.getItem().getId())
                                                                .itemName(questionItem.getItem().getName())
                                                                .celebName(questionItem.getItem().getBrand() != null
                                                                        ? questionItem.getItem().getBrand().getBrandKr()
                                                                        : questionItem.getItem().getNewBrand().getBrandName()
                                                                )
                                                                .brandName(questionItem.getItem().getBrand() != null
                                                                        ? questionItem.getItem().getBrand().getBrandKr()
                                                                        : questionItem.getItem().getNewBrand().getBrandName()
                                                                )
                                                                .imgUrl(itemImgRepository.findMainImg(questionItem.getItem().getId()).getItemImgUrl())
                                                                .scrapStatus(itemScrapRepository.getItemScrapStatus(questionItem.getItem(),closetList))
                                                                .build();
                                                QuestionItemResDto.QuestionItemResDtoBuilder builder = QuestionItemResDto.builder()
                                                        .item(dto)
                                                        .representFlag(questionItem.getRepresentFlag())
                                                        .sortOrder(questionItem.getSortOrder())
                                                        .description(questionItem.getDescription());

                                                // QuestionBuy일 경우 투표수 추가.

                                                if(qType != null & qType.equals("Buy")){
                                                    addVoteNum(questionId, (long) questionItem.getSortOrder(), null, builder);
                                                }

                                                return builder.build();
                                            }).toList();

        // Question Like Num Count
        Long questionLikeNum = questionLikeRepository.countByQuestionId(questionId);

        // Question Comment Num Count
        Long questionSearchNum = commentRepository.countByQuestionId(questionId);

        // hasLike 검색
        Boolean currentUserLike = questionLikeRepository.existsByQuestionIdAndUserId(questionId, user.getId());

        QuestionGetDetailResDto.QuestionGetDetailResDtoBuilder builder = QuestionGetDetailResDto.builder();
        if(qType.equals("Find")){
            QuestionFind questionFind = (QuestionFind) question;
            CelebChipResDto.CelebChipResDtoBuilder builder1 = CelebChipResDto.builder();
            if(questionFind.getCeleb() != null){
                CelebChipResDto dto = builder1.celebId(questionFind.getCeleb().getId()).celebName(questionFind.getCeleb().getCelebNameKr())
                        .build();
                builder
                        .celeb(dto)
                        .newCeleb(null);
            }else {
                CelebChipResDto dto = builder1.celebId(questionFind.getNewCeleb().getId()).celebName(questionFind.getNewCeleb().getCelebName())
                        .build();
                builder
                        .celeb(null)
                        .newCeleb(dto);
            }

            builder
                .voteEndTime(null);
        }else if(qType.equals("Buy")) {
            QuestionBuy questionBuy = (QuestionBuy) question;

            QuestionVote questionVote = questionVoteRepository.findByQuestionIdAndUserId(questionId, user.getId())
                                                                .orElse(null);

            builder
                .celeb(null)
                .newCeleb(null)
                .voteEndTime(questionBuy.getVoteEndTime())
                .totalVoteNum(questionVoteRepository.countByQuestionId(questionId))
                .voteStatus(
                        questionVote != null
                        ? questionVote.getVoteSortOrder()
                        : null
                );
        }else if(qType.equals("Recommend")) {
            List<String> categoryList = questionRecommendCategoryRepository.findAllByQuestionId(questionId)
                    .stream().map(QuestionRecommendCategory::getName).toList();
            builder.recommendCategoryList(categoryList);
        }else{
            builder.celeb(null)
                    .newCeleb(null)
                    .voteEndTime(null);
        }

        // RecentQuestion 등록
        recentQuestionRepository.save(
                RecentQuestion.builder()
                        .user(user)
                        .qType(qType)
                        .question(question)
                        .build()
        );

        // SearchNum 증가
        question.increaseSearchNum();

        return builder
                .qType(qType)
                .user(
                        UserInfoDto.builder()
                        .id(writer.getId())
                        .nickName(writer.getNickname())
                        .profileImgUrl(writer.getProfileImgUrl())
                        .build()
                )
                .title(question.getTitle())
                .content(question.getContent())
                .imgList(questionImgList)
                .itemList(questionItemList)
                .searchNum(question.getSearchNum())
                .likeNum(questionLikeNum)
                .commentNum(questionSearchNum)
                .createdAt(question.getCreatedAt())
                .hasLike(currentUserLike)
                .hasMine(user.getId().equals(writer.getId()))

                .build();
    }

    /**
     * builder에 VoteNum, VotePercent 탑재
     */
    private void addVoteNum(Long questionId, Long sortOrder, QuestionImgResDto.QuestionImgResDtoBuilder imgBuilder, QuestionItemResDto.QuestionItemResDtoBuilder itemBuilder) {


        // 해당 SortOrder의 투표 수
        Long voteNum = questionVoteRepository.countByQuestionIdAndVoteSortOrder(questionId, sortOrder);
        // 전체 투표 수
        Long totalVoteNum = questionVoteRepository.countByQuestionId(questionId);

        // builder 조립
        if(imgBuilder != null){
            imgBuilder
                .voteNum(voteNum)
                .votePercent(
                        totalVoteNum != 0
                                ? getVotePercent(voteNum, totalVoteNum)
                                : 0
                );
        }else if(itemBuilder != null){
            itemBuilder
                .voteNum(voteNum)
                .votePercent(
                        totalVoteNum != 0
                                ? getVotePercent(voteNum, totalVoteNum)
                                : 0
                );
        }else {
            throw new IllegalArgumentException();
        }

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
                    QuestionVote.builder()
                            .question(question)
                            .user(user)
                            .voteSortOrder(dto.getVoteSortOrder())
                            .build()
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
                .map(questionBuy -> {
                    // 이미지 URL
                    List<String> imgList = questionImgRepository.findAllByQuestionId(questionBuy.getId())
                            .stream().map(QuestionImg::getImgUrl).toList();
                    // 아이템 이미지 URL
                    List<String> itemImgList = questionItemRepository.findAllByQuestionId(questionBuy.getId())
                            .stream().map(questionItem ->
                                    itemImgRepository.findMainImg(questionItem.getItem().getId()).getItemImgUrl()
                            ).toList();

                    return QuestionSimpleResDto.builder()
                            .qType("Buy")
                            .id(questionBuy.getId())
                            .title(questionBuy.getTitle())
                            .content(questionBuy.getContent())
                            .imgList(imgList)
                            .itemImgList(itemImgList)
                            .build();
                }).toList();
    }

    /**
     * Wait QuestionRecommend 조회
     */

    public List<QuestionSimpleResDto> getWaitQuestionRecommend(User user, Long questionId) {
        return questionRepository.getWaitQuestionRecommend(user, questionId)
                .stream()
                .map(questionBuy -> {
                    // 이미지 URL
                    List<String> imgList = questionImgRepository.findAllByQuestionId(questionBuy.getId())
                            .stream().map(QuestionImg::getImgUrl).toList();
                    // 아이템 이미지 URL
                    List<String> itemImgList = questionItemRepository.findAllByQuestionId(questionBuy.getId())
                            .stream().map(questionItem ->
                                    itemImgRepository.findMainImg(questionItem.getItem().getId()).getItemImgUrl()
                            ).toList();

                    return QuestionSimpleResDto.builder()
                            .qType("Recommend")
                            .id(questionBuy.getId())
                            .title(questionBuy.getTitle())
                            .content(questionBuy.getContent())
                            .imgList(imgList)
                            .itemImgList(itemImgList)
                            .build();
                }).toList();
    }

    /**
     * Wait QuestionHowabout 조회
     */

    public List<QuestionSimpleResDto> getWaitQuestionHowabout(User user, Long questionId) {
        return questionRepository.getWaitQuestionHowabout(user, questionId)
                .stream()
                .map(questionBuy -> {
                    // 이미지 URL
                    List<String> imgList = questionImgRepository.findAllByQuestionId(questionBuy.getId())
                            .stream().map(QuestionImg::getImgUrl).toList();
                    // 아이템 이미지 URL
                    List<String> itemImgList = questionItemRepository.findAllByQuestionId(questionBuy.getId())
                            .stream().map(questionItem ->
                                    itemImgRepository.findMainImg(questionItem.getItem().getId()).getItemImgUrl()
                            ).toList();

                    return QuestionSimpleResDto.builder()
                            .qType("How")
                            .id(questionBuy.getId())
                            .title(questionBuy.getTitle())
                            .content(questionBuy.getContent())
                            .imgList(imgList)
                            .itemImgList(itemImgList)
                            .build();
                }).toList();
    }

    /**
     * Wait QuestionFind 조회
     */

    public List<QuestionSimpleResDto> getWaitQuestionFind(User user, Long questionId) {
        List<Celeb> interestedCeleb = celebRepository.findInterestedCeleb(user);

        return questionRepository.getWaitQuestionFind(user, questionId, interestedCeleb)
                .stream()
                .map(questionBuy -> {
                    // 이미지 URL
                    List<String> imgList = questionImgRepository.findAllByQuestionId(questionBuy.getId())
                            .stream().map(QuestionImg::getImgUrl).toList();
                    // 아이템 이미지 URL
                    List<String> itemImgList = questionItemRepository.findAllByQuestionId(questionBuy.getId())
                            .stream().map(questionItem ->
                                    itemImgRepository.findMainImg(questionItem.getItem().getId()).getItemImgUrl()
                            ).toList();

                    return QuestionSimpleResDto.builder()
                            .qType("Find")
                            .id(questionBuy.getId())
                            .title(questionBuy.getTitle())
                            .content(questionBuy.getContent())
                            .imgList(imgList)
                            .itemImgList(itemImgList)
                            .build();
                }).toList();
    }
}

