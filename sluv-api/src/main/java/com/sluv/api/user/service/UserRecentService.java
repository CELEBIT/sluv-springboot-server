package com.sluv.api.user.service;

import com.sluv.api.common.response.PaginationCountResponse;
import com.sluv.domain.comment.service.CommentDomainService;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemImg;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.item.service.ItemImgDomainService;
import com.sluv.domain.question.dto.QuestionImgSimpleDto;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.entity.*;
import com.sluv.domain.question.exception.QuestionTypeNotFoundException;
import com.sluv.domain.question.service.*;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRecentService {

    private final ItemDomainService itemDomainService;
    private final ItemImgDomainService itemImgDomainService;
    private final QuestionImgDomainService questionImgDomainService;
    private final QuestionItemDomainService questionItemDomainService;
    private final QuestionLikeDomainService questionLikeDomainService;
    private final RecentQuestionDomainService recentQuestionDomainService;
    private final QuestionRecommendCategoryDomainService questionRecommendCategoryDomainService;
    private final CommentDomainService commentDomainService;
    private final UserDomainService userDomainService;


    @Transactional(readOnly = true)
    public PaginationCountResponse<ItemSimpleDto> getUserRecentItem(Long userId, Pageable pageable) {
        User user = userDomainService.findById(userId);

        Page<Item> recentItemPage = itemDomainService.getUserAllRecentItem(user, pageable);

        List<ItemSimpleDto> content = itemDomainService.getItemSimpleDto(user, recentItemPage.getContent());

        return new PaginationCountResponse<>(recentItemPage.hasNext(), recentItemPage.getNumber(), content,
                recentItemPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public PaginationCountResponse<QuestionSimpleResDto> getUserRecentQuestion(Long userId, Pageable pageable) {
        User user = userDomainService.findById(userId);

        Page<Question> recentQuestionPage = recentQuestionDomainService.getUserAllRecentQuestion(user, pageable);

        List<QuestionSimpleResDto> content = recentQuestionPage.stream().map(question -> {
            List<QuestionImgSimpleDto> imgList = null;
            List<QuestionImgSimpleDto> itemImgList = null;
            List<String> categoryList = null;

            if (question instanceof QuestionBuy) {
                // 이미지 Dto 생성
                imgList = questionImgDomainService.findAllByQuestionId(question.getId()).stream()
                        .map(QuestionImgSimpleDto::of).toList();
                // 아이템 이미지 Dto 생성
                itemImgList = questionItemDomainService.findAllByQuestionId(question.getId()).stream()
                        .map(questionItem -> {
                            ItemImg mainImg = itemImgDomainService.findMainImg(questionItem.getItem().getId());
                            return QuestionImgSimpleDto.of(mainImg);
                        }).toList();

            } else if (question instanceof QuestionHowabout) {

            } else if (question instanceof QuestionRecommend) {
                // Question 카테고리
                categoryList = questionRecommendCategoryDomainService.findAllByQuestionId(question.getId()).stream()
                        .map(QuestionRecommendCategory::getName).toList();
            } else if (question instanceof QuestionFind) {

            } else {
                throw new QuestionTypeNotFoundException();
            }

            // Question 좋아요 수
            Long likeNum = questionLikeDomainService.countByQuestionId(question.getId());

            // Question 댓글 수
            Long commentNum = commentDomainService.countByQuestionId(question.getId());

            User writer = userDomainService.findByIdOrNull(question.getUser().getId());

            return QuestionSimpleResDto.of(question, writer, likeNum, commentNum, imgList, itemImgList, categoryList);


        }).toList();

        return new PaginationCountResponse<>(recentQuestionPage.hasNext(), recentQuestionPage.getNumber(), content,
                recentQuestionPage.getTotalElements());
    }
}
