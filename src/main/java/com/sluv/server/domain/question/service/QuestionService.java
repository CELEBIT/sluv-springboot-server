package com.sluv.server.domain.question.service;

import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.exception.ItemNotFoundException;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.question.dto.*;
import com.sluv.server.domain.question.entity.*;
import com.sluv.server.domain.question.enums.QuestionStatus;
import com.sluv.server.domain.question.repository.QuestionImgRepository;
import com.sluv.server.domain.question.repository.QuestionItemRepository;
import com.sluv.server.domain.question.repository.QuestionRecommendCategoryRepository;
import com.sluv.server.domain.question.repository.QuestionRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.enums.ItemImgOrLinkStatus;
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
    private final ItemRepository itemRepository;

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



}
