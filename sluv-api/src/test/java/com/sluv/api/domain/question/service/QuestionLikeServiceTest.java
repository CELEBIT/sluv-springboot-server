package com.sluv.api.domain.question.service;

import com.sluv.api.question.service.QuestionLikeService;
import com.sluv.domain.question.entity.QuestionHowabout;
import com.sluv.domain.question.service.QuestionDomainService;
import com.sluv.domain.question.service.QuestionLikeDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.infra.alarm.service.QuestionAlarmService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionLikeServiceTest {

    @InjectMocks
    private QuestionLikeService questionLikeService;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private QuestionDomainService questionDomainService;

    @Mock
    private QuestionLikeDomainService questionLikeDomainService;

    @Mock
    private QuestionAlarmService questionAlarmService;

    @Test
    @DisplayName("질문 좋아요 내역이 있으면 좋아요를 삭제한다.")
    void deleteQuestionLikeTest() {
        // given
        Long userId = 1L;
        Long questionId = 10L;
        User user = createUser(userId);
        QuestionHowabout question = createQuestion(questionId);

        when(userDomainService.findById(userId)).thenReturn(user);
        when(questionLikeDomainService.existsByQuestionIdAndUserId(questionId, userId)).thenReturn(true);
        when(questionDomainService.findById(questionId)).thenReturn(question);

        // when
        questionLikeService.postQuestionLike(userId, questionId);

        // then
        verify(questionLikeDomainService).deleteByQuestionIdAndUserId(questionId, userId);
        verify(questionLikeDomainService, never()).saveQuestionLike(
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any()
        );
        verify(questionAlarmService, never()).sendAlarmAboutQuestionLike(
                org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.anyLong()
        );
    }

    @Test
    @DisplayName("질문 좋아요 내역이 없으면 좋아요를 저장하고 알림을 보낸다.")
    void saveQuestionLikeTest() {
        // given
        Long userId = 1L;
        Long questionId = 10L;
        User user = createUser(userId);
        QuestionHowabout question = createQuestion(questionId);

        when(userDomainService.findById(userId)).thenReturn(user);
        when(questionLikeDomainService.existsByQuestionIdAndUserId(questionId, userId)).thenReturn(false);
        when(questionDomainService.findById(questionId)).thenReturn(question);

        // when
        questionLikeService.postQuestionLike(userId, questionId);

        // then
        verify(questionLikeDomainService).saveQuestionLike(user, question);
        verify(questionAlarmService).sendAlarmAboutQuestionLike(userId, questionId);
        verify(questionLikeDomainService, never()).deleteByQuestionIdAndUserId(
                org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.anyLong()
        );
    }

    private User createUser(Long id) {
        return User.builder()
                .id(id)
                .email("user@test.com")
                .build();
    }

    private QuestionHowabout createQuestion(Long id) {
        return QuestionHowabout.builder()
                .id(id)
                .title("질문 제목")
                .build();
    }
}
