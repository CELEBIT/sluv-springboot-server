package com.sluv.api.domain.question.service;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.question.helper.QuestionResponseHelper;
import com.sluv.api.question.service.QuestionFeedService;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.QuestionFind;
import com.sluv.domain.question.entity.QuestionHowabout;
import com.sluv.domain.question.entity.QuestionRecommend;
import com.sluv.domain.question.service.QuestionDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.entity.UserBlock;
import com.sluv.domain.user.service.UserBlockDomainService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionFeedServiceTest {

    @InjectMocks
    private QuestionFeedService questionFeedService;

    @Mock
    private QuestionDomainService questionDomainService;

    @Mock
    private UserBlockDomainService userBlockDomainService;

    @Mock
    private QuestionResponseHelper questionResponseHelper;

    @Test
    @DisplayName("전체 질문 피드를 조회한다.")
    void getTotalQuestionsTest() {
        // given
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        User user = createUser(userId);
        User blockedUser = createUser(2L);
        UserBlock userBlock = createUserBlock(user, blockedUser);
        Question question = QuestionHowabout.builder().id(10L).user(user).title("전체 질문").build();
        QuestionSimpleResDto response = createResponse(question.getId());
        Page<Question> questions = new PageImpl<>(List.of(question), pageable, 1);

        when(userBlockDomainService.getAllBlockedUser(userId)).thenReturn(List.of(userBlock));
        when(questionDomainService.getTotalQuestionList(List.of(blockedUser.getId()), pageable)).thenReturn(questions);
        whenQuestionResponseHelperReturns(questions, response);

        // when
        PaginationResponse<QuestionSimpleResDto> result = questionFeedService.getTotalQuestions(userId, pageable);

        // then
        assertThat(result.getContent()).containsExactly(response);
        verify(questionDomainService).getTotalQuestionList(List.of(blockedUser.getId()), pageable);
    }

    @Test
    @DisplayName("비회원 전체 질문 피드는 차단 유저를 조회하지 않는다.")
    void getTotalQuestionsWithoutUserTest() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        User writer = createUser(1L);
        Question question = QuestionHowabout.builder().id(11L).user(writer).title("전체 질문").build();
        QuestionSimpleResDto response = createResponse(question.getId());
        Page<Question> questions = new PageImpl<>(List.of(question), pageable, 1);

        when(questionDomainService.getTotalQuestionList(List.of(), pageable)).thenReturn(questions);
        whenQuestionResponseHelperReturns(questions, response);

        // when
        PaginationResponse<QuestionSimpleResDto> result = questionFeedService.getTotalQuestions(null, pageable);

        // then
        assertThat(result.getContent()).containsExactly(response);
        verify(userBlockDomainService, never()).getAllBlockedUser(org.mockito.ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("Find 질문 피드를 조회한다.")
    void getFindQuestionsTest() {
        // given
        Long userId = 1L;
        Long celebId = 20L;
        Boolean isNewCeleb = false;
        Pageable pageable = PageRequest.of(0, 10);
        User user = createUser(userId);
        QuestionFind question = QuestionFind.builder().id(12L).user(user).title("Find 질문").build();
        QuestionSimpleResDto response = createResponse(question.getId());
        Page<QuestionFind> questions = new PageImpl<>(List.of(question), pageable, 1);

        when(userBlockDomainService.getAllBlockedUser(userId)).thenReturn(List.of());
        when(questionDomainService.getQuestionFindList(celebId, isNewCeleb, List.of(), pageable))
                .thenReturn(questions);
        whenQuestionResponseHelperReturns(questions, response);

        // when
        PaginationResponse<QuestionSimpleResDto> result = questionFeedService.getFindQuestions(
                userId,
                celebId,
                isNewCeleb,
                pageable
        );

        // then
        assertThat(result.getContent()).containsExactly(response);
        verify(questionDomainService).getQuestionFindList(celebId, isNewCeleb, List.of(), pageable);
    }

    @Test
    @DisplayName("How 질문 피드를 조회한다.")
    void getHowaboutQuestionsTest() {
        // given
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        User user = createUser(userId);
        QuestionHowabout question = QuestionHowabout.builder().id(13L).user(user).title("How 질문").build();
        QuestionSimpleResDto response = createResponse(question.getId());
        Page<QuestionHowabout> questions = new PageImpl<>(List.of(question), pageable, 1);

        when(userBlockDomainService.getAllBlockedUser(userId)).thenReturn(List.of());
        when(questionDomainService.getQuestionHowaboutList(List.of(), pageable))
                .thenReturn(questions);
        whenQuestionResponseHelperReturns(questions, response);

        // when
        PaginationResponse<QuestionSimpleResDto> result = questionFeedService.getHowaboutQuestions(userId, pageable);

        // then
        assertThat(result.getContent()).containsExactly(response);
        verify(questionDomainService).getQuestionHowaboutList(List.of(), pageable);
    }

    @Test
    @DisplayName("Recommend 질문 피드를 조회한다.")
    void getRecommendQuestionsTest() {
        // given
        Long userId = 1L;
        String hashtag = "상의";
        Pageable pageable = PageRequest.of(0, 10);
        User user = createUser(userId);
        QuestionRecommend question = QuestionRecommend.builder().id(14L).user(user).title("Recommend 질문").build();
        QuestionSimpleResDto response = createResponse(question.getId());
        Page<QuestionRecommend> questions = new PageImpl<>(List.of(question), pageable, 1);

        when(userBlockDomainService.getAllBlockedUser(userId)).thenReturn(List.of());
        when(questionDomainService.getQuestionRecommendList(hashtag, List.of(), pageable))
                .thenReturn(questions);
        whenQuestionResponseHelperReturns(questions, response);

        // when
        PaginationResponse<QuestionSimpleResDto> result = questionFeedService.getRecommendQuestions(
                userId,
                hashtag,
                pageable
        );

        // then
        assertThat(result.getContent()).containsExactly(response);
        verify(questionDomainService).getQuestionRecommendList(hashtag, List.of(), pageable);
    }

    private User createUser(Long id) {
        return User.builder()
                .id(id)
                .email("user@test.com")
                .build();
    }

    private UserBlock createUserBlock(User user, User blockedUser) {
        return UserBlock.builder()
                .user(user)
                .blockedUser(blockedUser)
                .build();
    }

    private QuestionSimpleResDto createResponse(Long questionId) {
        return QuestionSimpleResDto.builder()
                .id(questionId)
                .title("질문 피드")
                .build();
    }

    private void whenQuestionResponseHelperReturns(Page<? extends Question> questions, QuestionSimpleResDto response) {
        PaginationResponse<QuestionSimpleResDto> paginationResponse = PaginationResponse.<QuestionSimpleResDto>builder()
                .page(questions.getNumber())
                .hasNext(questions.hasNext())
                .content(List.of(response))
                .build();

        when(questionResponseHelper.getQuestionSimpleResponsesWithMainImage(questions)).thenReturn(paginationResponse);
    }
}
