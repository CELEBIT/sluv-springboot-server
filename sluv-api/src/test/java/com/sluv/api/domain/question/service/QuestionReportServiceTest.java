package com.sluv.api.domain.question.service;

import com.sluv.api.question.dto.QuestionReportReqDto;
import com.sluv.api.question.service.QuestionReportService;
import com.sluv.domain.question.entity.QuestionHowabout;
import com.sluv.domain.question.enums.QuestionReportReason;
import com.sluv.domain.question.exception.QuestionReportDuplicateException;
import com.sluv.domain.question.service.QuestionDomainService;
import com.sluv.domain.question.service.QuestionReportDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionReportServiceTest {

    @InjectMocks
    private QuestionReportService questionReportService;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private QuestionDomainService questionDomainService;

    @Mock
    private QuestionReportDomainService questionReportDomainService;

    @Test
    @DisplayName("질문 신고 내역이 없으면 신고를 저장한다.")
    void postQuestionReportTest() {
        // given
        Long userId = 1L;
        Long questionId = 10L;
        User user = createUser(userId);
        QuestionHowabout question = createQuestion(questionId);
        QuestionReportReqDto request = createReportRequest();

        when(userDomainService.findById(userId)).thenReturn(user);
        when(questionReportDomainService.existsByQuestionIdAndReporterId(questionId, userId)).thenReturn(false);
        when(questionDomainService.findById(questionId)).thenReturn(question);

        // when
        questionReportService.postQuestionReport(userId, questionId, request);

        // then
        verify(questionReportDomainService).saveQuestionReport(
                user,
                question,
                request.getReason(),
                request.getContent()
        );
    }

    @Test
    @DisplayName("질문 신고 내역이 있으면 중복 신고 예외가 발생한다.")
    void postQuestionReportDuplicateTest() {
        // given
        Long userId = 1L;
        Long questionId = 10L;
        User user = createUser(userId);
        QuestionReportReqDto request = createReportRequest();

        when(userDomainService.findById(userId)).thenReturn(user);
        when(questionReportDomainService.existsByQuestionIdAndReporterId(questionId, userId)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> questionReportService.postQuestionReport(userId, questionId, request))
                .isInstanceOf(QuestionReportDuplicateException.class);

        verify(questionDomainService, never()).findById(org.mockito.ArgumentMatchers.anyLong());
        verify(questionReportDomainService, never()).saveQuestionReport(
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.anyString()
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

    private QuestionReportReqDto createReportRequest() {
        return QuestionReportReqDto.builder()
                .reason(QuestionReportReason.SPAM_OR_AD)
                .content("신고 내용")
                .build();
    }
}
