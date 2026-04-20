package com.sluv.api.domain.question.service;

import com.sluv.api.question.helper.QuestionResponseHelper;
import com.sluv.api.question.service.QuestionWaitService;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.service.CelebDomainService;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.entity.QuestionBuy;
import com.sluv.domain.question.entity.QuestionFind;
import com.sluv.domain.question.entity.QuestionHowabout;
import com.sluv.domain.question.entity.QuestionRecommend;
import com.sluv.domain.question.service.QuestionDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.entity.UserBlock;
import com.sluv.domain.user.service.UserBlockDomainService;
import com.sluv.domain.user.service.UserDomainService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionWaitServiceTest {

    @InjectMocks
    private QuestionWaitService questionWaitService;

    @Mock
    private QuestionDomainService questionDomainService;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private UserBlockDomainService userBlockDomainService;

    @Mock
    private CelebDomainService celebDomainService;

    @Mock
    private QuestionResponseHelper questionResponseHelper;

    @Test
    @DisplayName("Buy 대기 질문을 조회한다.")
    void getWaitQuestionBuyTest() {
        // given
        Long userId = 1L;
        Long questionId = 10L;
        User user = createUser(userId);
        User blockedUser = createUser(2L);
        UserBlock userBlock = createUserBlock(user, blockedUser);
        Celeb interestedCeleb = createCeleb(3L);
        QuestionBuy question = QuestionBuy.builder().id(100L).user(user).title("Buy 질문").build();
        QuestionSimpleResDto response = createResponse(question.getId());

        when(userDomainService.findById(userId)).thenReturn(user);
        when(userBlockDomainService.getAllBlockedUser(userId)).thenReturn(List.of(userBlock));
        when(celebDomainService.findInterestedCeleb(user)).thenReturn(List.of(interestedCeleb));
        when(questionDomainService.getWaitQuestionBuy(user, questionId, List.of(interestedCeleb), List.of(blockedUser.getId())))
                .thenReturn(List.of(question));
        when(questionResponseHelper.getQuestionSimpleResponseWithMainImage(question)).thenReturn(response);

        // when
        List<QuestionSimpleResDto> result = questionWaitService.getWaitQuestionBuy(userId, questionId);

        // then
        assertThat(result).containsExactly(response);
        verify(questionDomainService).getWaitQuestionBuy(user, questionId, List.of(interestedCeleb), List.of(blockedUser.getId()));
    }

    @Test
    @DisplayName("Find 대기 질문을 조회한다.")
    void getWaitQuestionFindTest() {
        // given
        Long userId = 1L;
        Long questionId = 10L;
        User user = createUser(userId);
        Celeb interestedCeleb = createCeleb(3L);
        QuestionFind question = QuestionFind.builder().id(101L).user(user).title("Find 질문").build();
        QuestionSimpleResDto response = createResponse(question.getId());

        when(userDomainService.findById(userId)).thenReturn(user);
        when(userBlockDomainService.getAllBlockedUser(userId)).thenReturn(List.of());
        when(celebDomainService.findInterestedCeleb(user)).thenReturn(List.of(interestedCeleb));
        when(questionDomainService.getWaitQuestionFind(user, questionId, List.of(interestedCeleb), List.of()))
                .thenReturn(List.of(question));
        when(questionResponseHelper.getQuestionSimpleResponseWithMainImage(question)).thenReturn(response);

        // when
        List<QuestionSimpleResDto> result = questionWaitService.getWaitQuestionFind(userId, questionId);

        // then
        assertThat(result).containsExactly(response);
        verify(questionDomainService).getWaitQuestionFind(user, questionId, List.of(interestedCeleb), List.of());
    }

    @Test
    @DisplayName("How 대기 질문을 조회한다.")
    void getWaitQuestionHowaboutTest() {
        // given
        Long userId = 1L;
        Long questionId = 10L;
        User user = createUser(userId);
        QuestionHowabout question = QuestionHowabout.builder().id(102L).user(user).title("How 질문").build();
        QuestionSimpleResDto response = createResponse(question.getId());

        when(userDomainService.findById(userId)).thenReturn(user);
        when(userBlockDomainService.getAllBlockedUser(userId)).thenReturn(List.of());
        when(questionDomainService.getWaitQuestionHowabout(user, questionId, List.of()))
                .thenReturn(List.of(question));
        when(questionResponseHelper.getQuestionSimpleResponseWithMainImage(question)).thenReturn(response);

        // when
        List<QuestionSimpleResDto> result = questionWaitService.getWaitQuestionHowabout(userId, questionId);

        // then
        assertThat(result).containsExactly(response);
        verify(questionDomainService).getWaitQuestionHowabout(user, questionId, List.of());
        verify(celebDomainService, never()).findInterestedCeleb(user);
    }

    @Test
    @DisplayName("Recommend 대기 질문을 조회한다.")
    void getWaitQuestionRecommendTest() {
        // given
        Long userId = 1L;
        Long questionId = 10L;
        User user = createUser(userId);
        QuestionRecommend question = QuestionRecommend.builder().id(103L).user(user).title("Recommend 질문").build();
        QuestionSimpleResDto response = createResponse(question.getId());

        when(userDomainService.findById(userId)).thenReturn(user);
        when(userBlockDomainService.getAllBlockedUser(userId)).thenReturn(List.of());
        when(questionDomainService.getWaitQuestionRecommend(user, questionId, List.of()))
                .thenReturn(List.of(question));
        when(questionResponseHelper.getQuestionSimpleResponseWithMainImage(question)).thenReturn(response);

        // when
        List<QuestionSimpleResDto> result = questionWaitService.getWaitQuestionRecommend(userId, questionId);

        // then
        assertThat(result).containsExactly(response);
        verify(questionDomainService).getWaitQuestionRecommend(user, questionId, List.of());
        verify(celebDomainService, never()).findInterestedCeleb(user);
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

    private Celeb createCeleb(Long id) {
        return Celeb.builder()
                .id(id)
                .celebNameKr("셀럽")
                .celebNameEn("celeb")
                .build();
    }

    private QuestionSimpleResDto createResponse(Long questionId) {
        return QuestionSimpleResDto.builder()
                .id(questionId)
                .title("대기 질문")
                .build();
    }
}
