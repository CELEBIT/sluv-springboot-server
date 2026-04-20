package com.sluv.api.domain.question.helper;

import com.sluv.api.question.dto.QuestionImgReqDto;
import com.sluv.api.question.dto.QuestionImgResDto;
import com.sluv.api.question.dto.QuestionVoteDataDto;
import com.sluv.api.question.helper.QuestionImageManager;
import com.sluv.domain.item.repository.ItemImgRepository;
import com.sluv.domain.question.entity.QuestionHowabout;
import com.sluv.domain.question.entity.QuestionImg;
import com.sluv.domain.question.repository.QuestionImgRepository;
import com.sluv.domain.question.repository.QuestionItemRepository;
import com.sluv.domain.question.service.QuestionImgDomainService;
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
public class QuestionImageManagerTest {

    @InjectMocks
    private QuestionImageManager questionImageManager;

    @Mock
    private QuestionImgDomainService questionImgDomainService;

    @Mock
    private QuestionImgRepository questionImgRepository;

    @Mock
    private QuestionItemRepository questionItemRepository;

    @Mock
    private ItemImgRepository itemImgRepository;

    @Test
    @DisplayName("질문 이미지 목록이 null이면 기존 이미지만 삭제한다.")
    void saveImagesWithNullRequestTest() {
        // given
        QuestionHowabout question = createQuestion(1L);

        // when
        questionImageManager.saveImages(null, question);

        // then
        verify(questionImgDomainService).deleteAllByQuestionId(question.getId());
        verify(questionImgDomainService, never()).saveAll(org.mockito.ArgumentMatchers.anyList());
    }

    @Test
    @DisplayName("질문 이미지 목록을 저장한다.")
    void saveImagesTest() {
        // given
        QuestionHowabout question = createQuestion(1L);
        List<QuestionImgReqDto> imageRequests = List.of(
                QuestionImgReqDto.builder()
                        .imgUrl("https://image.test/1.jpg")
                        .description("대표 이미지")
                        .representFlag(true)
                        .sortOrder(1)
                        .build(),
                QuestionImgReqDto.builder()
                        .imgUrl("https://image.test/2.jpg")
                        .description("추가 이미지")
                        .representFlag(false)
                        .sortOrder(2)
                        .build()
        );

        // when
        questionImageManager.saveImages(imageRequests, question);

        // then
        verify(questionImgDomainService).deleteAllByQuestionId(question.getId());

        ArgumentCaptor<List<QuestionImg>> captor = ArgumentCaptor.forClass(List.class);
        verify(questionImgDomainService).saveAll(captor.capture());

        List<QuestionImg> savedQuestionImages = captor.getValue();
        assertThat(savedQuestionImages).hasSize(2);
        assertThat(savedQuestionImages.get(0).getQuestion()).isEqualTo(question);
        assertThat(savedQuestionImages.get(0).getImgUrl()).isEqualTo("https://image.test/1.jpg");
        assertThat(savedQuestionImages.get(0).getDescription()).isEqualTo("대표 이미지");
        assertThat(savedQuestionImages.get(0).getRepresentFlag()).isTrue();
        assertThat(savedQuestionImages.get(0).getSortOrder()).isEqualTo(1);
        assertThat(savedQuestionImages.get(1).getQuestion()).isEqualTo(question);
        assertThat(savedQuestionImages.get(1).getImgUrl()).isEqualTo("https://image.test/2.jpg");
        assertThat(savedQuestionImages.get(1).getDescription()).isEqualTo("추가 이미지");
        assertThat(savedQuestionImages.get(1).getRepresentFlag()).isFalse();
        assertThat(savedQuestionImages.get(1).getSortOrder()).isEqualTo(2);
    }

    @Test
    @DisplayName("질문 이미지 응답에 투표 데이터를 포함한다.")
    void getQuestionImageResponsesTest() {
        // given
        Long questionId = 1L;
        QuestionHowabout question = createQuestion(questionId);
        QuestionImg questionImg = QuestionImg.builder()
                .question(question)
                .imgUrl("https://question-image.test/detail.jpg")
                .description("상세 이미지")
                .sortOrder(2)
                .representFlag(true)
                .build();
        QuestionVoteDataDto voteData = QuestionVoteDataDto.builder()
                .voteNum(3L)
                .votePercent(75.0)
                .build();

        when(questionImgRepository.findAllByQuestionId(questionId)).thenReturn(List.of(questionImg));

        // when
        List<QuestionImgResDto> responses = questionImageManager.getQuestionImageResponses(
                questionId,
                sortOrder -> voteData
        );

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getImgUrl()).isEqualTo("https://question-image.test/detail.jpg");
        assertThat(responses.get(0).getDescription()).isEqualTo("상세 이미지");
        assertThat(responses.get(0).getRepresentFlag()).isTrue();
        assertThat(responses.get(0).getSortOrder()).isEqualTo(2);
        assertThat(responses.get(0).getVoteNum()).isEqualTo(3L);
        assertThat(responses.get(0).getVotePercent()).isEqualTo(75.0);
    }

    private QuestionHowabout createQuestion(Long id) {
        return QuestionHowabout.builder()
                .id(id)
                .title("질문 제목")
                .content("질문 내용")
                .build();
    }
}
