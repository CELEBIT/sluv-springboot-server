package com.sluv.api.question.service;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.question.dto.QuestionBuySimpleResDto;
import com.sluv.api.question.dto.QuestionImgResDto;
import com.sluv.api.question.dto.QuestionItemResDto;
import com.sluv.api.question.dto.QuestionVoteDataDto;
import com.sluv.api.question.helper.QuestionResponseAssembler;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.item.service.ItemImgDomainService;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.QuestionBuy;
import com.sluv.domain.question.entity.QuestionFind;
import com.sluv.domain.question.entity.QuestionHowabout;
import com.sluv.domain.question.entity.QuestionImg;
import com.sluv.domain.question.entity.QuestionItem;
import com.sluv.domain.question.entity.QuestionRecommend;
import com.sluv.domain.question.entity.QuestionVote;
import com.sluv.domain.question.service.QuestionDomainService;
import com.sluv.domain.question.service.QuestionImgDomainService;
import com.sluv.domain.question.service.QuestionItemDomainService;
import com.sluv.domain.question.service.QuestionVoteDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserBlockDomainService;
import com.sluv.domain.user.service.UserDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionFeedService {

    private final QuestionDomainService questionDomainService;
    private final UserBlockDomainService userBlockDomainService;
    private final QuestionResponseAssembler questionResponseAssembler;
    private final QuestionImgDomainService questionImgDomainService;
    private final QuestionItemDomainService questionItemDomainService;
    private final ItemImgDomainService itemImgDomainService;
    private final QuestionVoteDomainService questionVoteDomainService;
    private final UserDomainService userDomainService;
    private final QuestionVoteService questionVoteService;

    @Transactional(readOnly = true)
    public PaginationResponse<QuestionSimpleResDto> getTotalQuestions(Long userId, Pageable pageable) {
        List<Long> blockedUserIds = getBlockedUserIds(userId);
        Page<Question> questions = questionDomainService.getTotalQuestionList(blockedUserIds, pageable);

        return toQuestionSimpleResponse(questions);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<QuestionSimpleResDto> getFindQuestions(Long userId, Long celebId, Boolean isNewCeleb,
                                                                     Pageable pageable) {
        List<Long> blockedUserIds = getBlockedUserIds(userId);
        Page<QuestionFind> questions = questionDomainService.getQuestionFindList(
                celebId,
                isNewCeleb,
                blockedUserIds,
                pageable
        );

        return toQuestionSimpleResponse(questions);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<QuestionSimpleResDto> getHowaboutQuestions(Long userId, Pageable pageable) {
        List<Long> blockedUserIds = getBlockedUserIds(userId);
        Page<QuestionHowabout> questions = questionDomainService.getQuestionHowaboutList(blockedUserIds, pageable);

        return toQuestionSimpleResponse(questions);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<QuestionSimpleResDto> getRecommendQuestions(Long userId, String hashtag,
                                                                          Pageable pageable) {
        List<Long> blockedUserIds = getBlockedUserIds(userId);
        Page<QuestionRecommend> questions = questionDomainService.getQuestionRecommendList(
                hashtag,
                blockedUserIds,
                pageable
        );

        return toQuestionSimpleResponse(questions);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<QuestionBuySimpleResDto> getBuyQuestions(Long userId, String voteStatus,
                                                                       Pageable pageable) {
        User user = userDomainService.findById(userId);
        List<Long> blockedUserIds = getBlockedUserIds(userId);
        Page<QuestionBuy> questions = questionDomainService.getQuestionBuyList(voteStatus, blockedUserIds, pageable);
        List<QuestionBuySimpleResDto> questionResponses = questions.stream()
                .map(question -> getBuyQuestionResponse(user, question))
                .toList();

        return PaginationResponse.of(questions, questionResponses);
    }

    private List<Long> getBlockedUserIds(Long userId) {
        if (userId == null) {
            return List.of();
        }

        return userBlockDomainService.getAllBlockedUser(userId).stream()
                .map(userBlock -> userBlock.getBlockedUser().getId())
                .toList();
    }

    private PaginationResponse<QuestionSimpleResDto> toQuestionSimpleResponse(Page<? extends Question> questions) {
        List<QuestionSimpleResDto> questionResponses = questions.stream()
                .map(questionResponseAssembler::getQuestionSimpleResponseWithMainImage)
                .toList();

        return PaginationResponse.of(questions, questionResponses);
    }

    private QuestionBuySimpleResDto getBuyQuestionResponse(User user, QuestionBuy question) {
        List<QuestionImgResDto> questionImages = getBuyQuestionImages(question);
        List<QuestionItemResDto> questionItems = getBuyQuestionItems(question);
        Long voteCount = questionVoteService.getTotalVoteCount(questionImages, questionItems);
        QuestionVote questionVote = getQuestionVote(user, question);
        User writer = userDomainService.findByIdOrNull(question.getUser().getId());

        return QuestionBuySimpleResDto.of(
                user,
                question,
                writer,
                voteCount,
                questionImages,
                questionItems,
                question.getVoteEndTime(),
                questionVote
        );
    }

    private List<QuestionImgResDto> getBuyQuestionImages(QuestionBuy question) {
        return questionImgDomainService.findAllByQuestionId(question.getId()).stream()
                .map(this::getBuyQuestionImage)
                .toList();
    }

    private QuestionImgResDto getBuyQuestionImage(QuestionImg questionImg) {
        QuestionVoteDataDto voteData = questionVoteService.getVoteData(
                questionImg.getQuestion().getId(),
                (long) questionImg.getSortOrder()
        );

        return QuestionImgResDto.of(questionImg, voteData);
    }

    private List<QuestionItemResDto> getBuyQuestionItems(QuestionBuy question) {
        return questionItemDomainService.findAllByQuestionId(question.getId()).stream()
                .map(this::getBuyQuestionItem)
                .toList();
    }

    private QuestionItemResDto getBuyQuestionItem(QuestionItem questionItem) {
        ItemSimpleDto item = ItemSimpleDto.of(
                questionItem.getItem(),
                itemImgDomainService.findMainImg(questionItem.getItem().getId()),
                null
        );
        QuestionVoteDataDto voteData = questionVoteService.getVoteData(
                questionItem.getQuestion().getId(),
                (long) questionItem.getSortOrder()
        );

        return QuestionItemResDto.of(questionItem, item, voteData);
    }

    private QuestionVote getQuestionVote(User user, QuestionBuy question) {
        if (user == null) {
            return null;
        }

        return questionVoteDomainService.findByQuestionIdAndUserIdOrNull(question.getId(), user.getId());
    }

}
