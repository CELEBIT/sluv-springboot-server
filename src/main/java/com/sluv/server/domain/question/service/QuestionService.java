package com.sluv.server.domain.question.service;

import com.sluv.server.domain.comment.repository.CommentRepository;
import com.sluv.server.domain.item.dto.ItemSameResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.ItemImg;
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
import jakarta.persistence.DiscriminatorColumn;
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
        QuestionFind questionFind = questionFindBuilder
                .user(user)
                .title(dto.getTitle())
                .content(dto.getContent())
                .searchNum(0L)
                .celebId(dto.getCelebId())
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
                .vote(imgDto.getVote())
                .representFlag(imgDto.getRepresentFlag())
                .itemImgOrLinkStatus(ItemImgOrLinkStatus.ACTIVE)
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
                            .vote(itemDto.getVote())
                            .representFlag(itemDto.getRepresentFlag())
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


        if(likeStatus) {
            // like가 있다면 삭제
            questionLikeRepository.deleteByQuestionIdAndUserId(questionId, user.getId());
        }else {
            // like가 없다면 등록
            Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

            questionLikeRepository.save(
                    QuestionLike.builder()
                            .user(user)
                            .question(question)
                            .build()
            );
        }
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

    public QuestionGetDetailResDto getQuestionDetail(User user, Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

        // Question Type 분류
        String qType = null;
        if(question != null){
            if (question instanceof QuestionFind){
                qType = "Find";
            }else if(question instanceof QuestionBuy){
                qType = "Buy";
            }else if(question instanceof QuestionHowabout){
                qType = "How";
            }else if(question instanceof QuestionRecommend){
                qType = "Recommend";
            }
        }

        // 작성자
        User writer = question.getUser();

        // Question img List
        List<String> questionImgList= questionImgRepository.findAllByQuestionId(questionId).
                                                            stream()
                                                            .map(QuestionImg::getImgUrl).toList();

        // Question Item List
        List<ItemSameResDto> questionItemList = questionItemRepository.findAllByQuestionId(questionId)
                                            .stream()
                                            .map(questionItem ->
                                                    ItemSameResDto.builder()
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
                                                                .scrapStatus(null) // 추후 작성 (23.05.20)
                                                        .build()
                                            ).toList();

        // Question Like Num Count
        Long questionLikeNum = questionLikeRepository.countByQuestionId(questionId);

        // Question Comment Num Count
        Long questionSearchNum = commentRepository.countByQuestionId(questionId);

        // hasLike 검색
        Boolean currentUserLike = questionLikeRepository.existsByQuestionIdAndUserId(questionId, user.getId());


        return QuestionGetDetailResDto.builder()
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
}
