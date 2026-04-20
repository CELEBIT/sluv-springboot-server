package com.sluv.api.question.helper;

import com.sluv.api.question.dto.QuestionItemResDto;
import com.sluv.api.question.dto.QuestionItemReqDto;
import com.sluv.api.question.dto.QuestionVoteDataDto;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.item.service.ItemImgDomainService;
import com.sluv.domain.item.service.ItemScrapDomainService;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.QuestionItem;
import com.sluv.domain.question.service.QuestionItemDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class QuestionItemManager {

    private final QuestionItemDomainService questionItemDomainService;
    private final ItemDomainService itemDomainService;
    private final ItemImgDomainService itemImgDomainService;
    private final ItemScrapDomainService itemScrapDomainService;

    public void saveItems(List<QuestionItemReqDto> itemRequests, Question question) {
        questionItemDomainService.deleteAllByQuestionId(question.getId());

        if (itemRequests != null) {
            List<QuestionItem> questionItems = itemRequests.stream()
                    .map(itemRequest -> {
                        Item item = itemDomainService.findById(itemRequest.getItemId());

                        return QuestionItem.toEntity(
                                question,
                                item,
                                itemRequest.getDescription(),
                                itemRequest.getRepresentFlag(),
                                itemRequest.getSortOrder()
                        );
                    })
                    .toList();

            questionItemDomainService.saveAll(questionItems);
        }
    }

    public List<QuestionItemResDto> getQuestionItemResponses(Long questionId, List<Closet> closets,
                                                             Function<Integer, QuestionVoteDataDto> voteDataResolver) {
        return questionItemDomainService.findAllByQuestionId(questionId)
                .stream()
                .map(questionItem -> getQuestionItemResponse(questionItem, closets, voteDataResolver))
                .toList();
    }

    private QuestionItemResDto getQuestionItemResponse(QuestionItem questionItem, List<Closet> closets,
                                                       Function<Integer, QuestionVoteDataDto> voteDataResolver) {
        ItemSimpleDto item = ItemSimpleDto.of(
                questionItem.getItem(),
                itemImgDomainService.findMainImg(questionItem.getItem().getId()),
                itemScrapDomainService.getItemScrapStatus(questionItem.getItem(), closets)
        );

        return QuestionItemResDto.of(questionItem, item, voteDataResolver.apply(questionItem.getSortOrder()));
    }
}
