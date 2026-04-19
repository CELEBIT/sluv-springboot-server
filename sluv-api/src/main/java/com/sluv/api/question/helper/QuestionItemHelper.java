package com.sluv.api.question.helper;

import com.sluv.api.question.dto.QuestionItemReqDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.QuestionItem;
import com.sluv.domain.question.service.QuestionItemDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionItemHelper {

    private final QuestionItemDomainService questionItemDomainService;
    private final ItemDomainService itemDomainService;

    public void saveQuestionItem(List<QuestionItemReqDto> itemRequests, Question question) {
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
}
