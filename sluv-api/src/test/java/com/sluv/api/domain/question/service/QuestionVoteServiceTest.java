package com.sluv.api.domain.question.service;

import com.sluv.api.question.dto.QuestionVoteDataDto;
import com.sluv.api.question.dto.QuestionImgResDto;
import com.sluv.api.question.dto.QuestionItemResDto;
import com.sluv.api.question.dto.QuestionVoteReqDto;
import com.sluv.api.question.service.QuestionVoteService;
import com.sluv.domain.question.entity.QuestionBuy;
import com.sluv.domain.question.entity.QuestionVote;
import com.sluv.domain.question.service.QuestionDomainService;
import com.sluv.domain.question.service.QuestionVoteDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionVoteServiceTest {

    @InjectMocks
    private QuestionVoteService questionVoteService;

    @Mock
    private QuestionDomainService questionDomainService;

    @Mock
    private QuestionVoteDomainService questionVoteDomainService;

    @Mock
    private UserDomainService userDomainService;

    @Test
    @DisplayName("투표 내역이 없으면 질문에 투표한다.")
    void postQuestionVoteTest() {
        // given
        Long userId = 1L;
        Long questionId = 10L;
        Long voteSortOrder = 2L;
        User user = createUser(userId);
        QuestionBuy question = createQuestion(questionId);
        QuestionVoteReqDto request = createVoteRequest(voteSortOrder);

        when(userDomainService.findById(userId)).thenReturn(user);
        when(questionVoteDomainService.findByQuestionIdAndUserIdOrNull(questionId, userId)).thenReturn(null);
        when(questionDomainService.findById(questionId)).thenReturn(question);

        // when
        questionVoteService.postQuestionVote(userId, questionId, request);

        // then
        verify(questionVoteDomainService).saveQuestionVote(question, user, voteSortOrder);
        verify(questionVoteDomainService, never()).deleteById(org.mockito.ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("투표 내역이 있으면 기존 투표를 취소한다.")
    void cancelQuestionVoteTest() {
        // given
        Long userId = 1L;
        Long questionId = 10L;
        User user = createUser(userId);
        QuestionVote questionVote = createQuestionVote(100L, 2L);
        QuestionVoteReqDto request = createVoteRequest(2L);

        when(userDomainService.findById(userId)).thenReturn(user);
        when(questionVoteDomainService.findByQuestionIdAndUserIdOrNull(questionId, userId)).thenReturn(questionVote);

        // when
        questionVoteService.postQuestionVote(userId, questionId, request);

        // then
        verify(questionVoteDomainService).deleteById(questionVote.getId());
        verify(questionDomainService, never()).findById(org.mockito.ArgumentMatchers.anyLong());
        verify(questionVoteDomainService, never()).saveQuestionVote(
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.anyLong()
        );
    }

    @Test
    @DisplayName("특정 투표 순서의 투표 수와 퍼센트를 계산한다.")
    void getVoteDataTest() {
        // given
        Long questionId = 10L;
        Long voteSortOrder = 2L;
        List<QuestionVote> questionVotes = List.of(
                createQuestionVote(1L, 1L),
                createQuestionVote(2L, 2L),
                createQuestionVote(3L, 2L)
        );

        when(questionVoteDomainService.findAllByQuestionId(questionId)).thenReturn(questionVotes);

        // when
        QuestionVoteDataDto voteData = questionVoteService.getVoteData(questionId, voteSortOrder);

        // then
        assertThat(voteData.getVoteNum()).isEqualTo(2L);
        assertThat(voteData.getVotePercent()).isEqualTo(66.7);
    }

    @Test
    @DisplayName("투표가 없으면 투표 수와 퍼센트를 0으로 반환한다.")
    void getVoteDataWithEmptyVoteTest() {
        // given
        Long questionId = 10L;
        Long voteSortOrder = 2L;

        when(questionVoteDomainService.findAllByQuestionId(questionId)).thenReturn(List.of());

        // when
        QuestionVoteDataDto voteData = questionVoteService.getVoteData(questionId, voteSortOrder);

        // then
        assertThat(voteData.getVoteNum()).isEqualTo(0L);
        assertThat(voteData.getVotePercent()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("질문 이미지와 아이템의 총 투표 수를 계산한다.")
    void getTotalVoteCountTest() {
        // given
        List<QuestionImgResDto> questionImages = List.of(
                QuestionImgResDto.builder().voteNum(1L).build(),
                QuestionImgResDto.builder().voteNum(2L).build()
        );
        List<QuestionItemResDto> questionItems = List.of(
                QuestionItemResDto.builder().voteNum(3L).build(),
                QuestionItemResDto.builder().voteNum(4L).build()
        );

        // when
        Long totalVoteCount = questionVoteService.getTotalVoteCount(questionImages, questionItems);

        // then
        assertThat(totalVoteCount).isEqualTo(10L);
    }

    private User createUser(Long id) {
        return User.builder()
                .id(id)
                .email("user@test.com")
                .build();
    }

    private QuestionBuy createQuestion(Long id) {
        return QuestionBuy.builder()
                .id(id)
                .title("질문 제목")
                .build();
    }

    private QuestionVoteReqDto createVoteRequest(Long voteSortOrder) {
        QuestionVoteReqDto request = new QuestionVoteReqDto();
        request.setVoteSortOrder(voteSortOrder);
        return request;
    }

    private QuestionVote createQuestionVote(Long id, Long voteSortOrder) {
        return QuestionVote.builder()
                .id(id)
                .voteSortOrder(voteSortOrder)
                .build();
    }
}
