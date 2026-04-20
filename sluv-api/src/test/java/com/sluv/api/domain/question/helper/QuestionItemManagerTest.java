package com.sluv.api.domain.question.helper;

import com.sluv.api.question.dto.QuestionItemResDto;
import com.sluv.api.question.dto.QuestionItemReqDto;
import com.sluv.api.question.dto.QuestionVoteDataDto;
import com.sluv.api.question.helper.QuestionItemManager;
import com.sluv.domain.brand.entity.NewBrand;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemImg;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.item.service.ItemImgDomainService;
import com.sluv.domain.item.service.ItemScrapDomainService;
import com.sluv.domain.question.entity.QuestionHowabout;
import com.sluv.domain.question.entity.QuestionItem;
import com.sluv.domain.question.service.QuestionItemDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionItemManagerTest {

    @InjectMocks
    private QuestionItemManager questionItemManager;

    @Mock
    private QuestionItemDomainService questionItemDomainService;

    @Mock
    private ItemDomainService itemDomainService;

    @Mock
    private ItemImgDomainService itemImgDomainService;

    @Mock
    private ItemScrapDomainService itemScrapDomainService;

    @Test
    @DisplayName("질문 아이템 목록이 null이면 기존 아이템만 삭제한다.")
    void saveItemsWithNullRequestTest() {
        // given
        QuestionHowabout question = createQuestion(1L);

        // when
        questionItemManager.saveItems(null, question);

        // then
        verify(questionItemDomainService).deleteAllByQuestionId(question.getId());
        verify(itemDomainService, never()).findById(org.mockito.ArgumentMatchers.anyLong());
        verify(questionItemDomainService, never()).saveAll(org.mockito.ArgumentMatchers.anyList());
    }

    @Test
    @DisplayName("질문 아이템 목록을 저장한다.")
    void saveItemsTest() {
        // given
        QuestionHowabout question = createQuestion(1L);
        Item firstItem = createItem(10L, "첫 번째 아이템");
        Item secondItem = createItem(20L, "두 번째 아이템");
        List<QuestionItemReqDto> itemRequests = List.of(
                QuestionItemReqDto.builder()
                        .itemId(firstItem.getId())
                        .description("대표 아이템")
                        .representFlag(true)
                        .sortOrder(1)
                        .build(),
                QuestionItemReqDto.builder()
                        .itemId(secondItem.getId())
                        .description("추가 아이템")
                        .representFlag(false)
                        .sortOrder(2)
                        .build()
        );

        when(itemDomainService.findById(firstItem.getId())).thenReturn(firstItem);
        when(itemDomainService.findById(secondItem.getId())).thenReturn(secondItem);

        // when
        questionItemManager.saveItems(itemRequests, question);

        // then
        verify(questionItemDomainService).deleteAllByQuestionId(question.getId());
        verify(itemDomainService).findById(firstItem.getId());
        verify(itemDomainService).findById(secondItem.getId());

        ArgumentCaptor<List<QuestionItem>> captor = ArgumentCaptor.forClass(List.class);
        verify(questionItemDomainService).saveAll(captor.capture());

        List<QuestionItem> savedQuestionItems = captor.getValue();
        assertThat(savedQuestionItems).hasSize(2);
        assertThat(savedQuestionItems.get(0).getQuestion()).isEqualTo(question);
        assertThat(savedQuestionItems.get(0).getItem()).isEqualTo(firstItem);
        assertThat(savedQuestionItems.get(0).getDescription()).isEqualTo("대표 아이템");
        assertThat(savedQuestionItems.get(0).getRepresentFlag()).isTrue();
        assertThat(savedQuestionItems.get(0).getSortOrder()).isEqualTo(1);
        assertThat(savedQuestionItems.get(1).getQuestion()).isEqualTo(question);
        assertThat(savedQuestionItems.get(1).getItem()).isEqualTo(secondItem);
        assertThat(savedQuestionItems.get(1).getDescription()).isEqualTo("추가 아이템");
        assertThat(savedQuestionItems.get(1).getRepresentFlag()).isFalse();
        assertThat(savedQuestionItems.get(1).getSortOrder()).isEqualTo(2);
    }

    @Test
    @DisplayName("질문 아이템 응답에 아이템 정보와 투표 데이터를 포함한다.")
    void getQuestionItemResponsesTest() {
        // given
        Long questionId = 1L;
        QuestionHowabout question = createQuestion(questionId);
        Item item = createItem(10L, "아이템");
        QuestionItem questionItem = QuestionItem.builder()
                .question(question)
                .item(item)
                .description("질문 아이템")
                .representFlag(true)
                .sortOrder(2)
                .build();
        ItemImg mainImage = ItemImg.builder()
                .item(item)
                .itemImgUrl("https://item-image.test/main.jpg")
                .representFlag(true)
                .sortOrder(1)
                .build();
        QuestionVoteDataDto voteData = QuestionVoteDataDto.builder()
                .voteNum(5L)
                .votePercent(50.0)
                .build();

        when(questionItemDomainService.findAllByQuestionId(questionId)).thenReturn(List.of(questionItem));
        when(itemImgDomainService.findMainImg(item.getId())).thenReturn(mainImage);
        when(itemScrapDomainService.getItemScrapStatus(item, List.of())).thenReturn(false);

        // when
        List<QuestionItemResDto> responses = questionItemManager.getQuestionItemResponses(
                questionId,
                List.of(),
                sortOrder -> voteData
        );

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getItem().getItemId()).isEqualTo(item.getId());
        assertThat(responses.get(0).getItem().getImgUrl()).isEqualTo("https://item-image.test/main.jpg");
        assertThat(responses.get(0).getDescription()).isEqualTo("질문 아이템");
        assertThat(responses.get(0).getRepresentFlag()).isTrue();
        assertThat(responses.get(0).getSortOrder()).isEqualTo(2);
        assertThat(responses.get(0).getVoteNum()).isEqualTo(5L);
        assertThat(responses.get(0).getVotePercent()).isEqualTo(50.0);
    }

    private QuestionHowabout createQuestion(Long id) {
        return QuestionHowabout.builder()
                .id(id)
                .title("질문 제목")
                .content("질문 내용")
                .build();
    }

    private Item createItem(Long id, String name) {
        return Item.builder()
                .id(id)
                .newBrand(NewBrand.builder()
                        .id(30L)
                        .brandName("브랜드")
                        .build())
                .newCeleb(NewCeleb.builder()
                        .id(40L)
                        .celebName("셀럽")
                        .build())
                .name(name)
                .price(0)
                .build();
    }
}
