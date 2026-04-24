package com.sluv.api.domain.question.service;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.question.dto.QuestionHomeResDto;
import com.sluv.api.question.helper.QuestionResponseAssembler;
import com.sluv.api.question.service.QuestionRankService;
import com.sluv.domain.auth.enums.SnsType;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.entity.QuestionBuy;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionRankServiceTest {

    @InjectMocks
    private QuestionRankService questionRankService;

    @Mock
    private QuestionDomainService questionDomainService;

    @Mock
    private UserBlockDomainService userBlockDomainService;

    @Mock
    private QuestionResponseAssembler questionResponseAssembler;

    @Test
    @DisplayName("일간 인기 질문을 조회한다.")
    void getDailyHotQuestionsTest() {
        // given
        Long userId = 1L;
        User user = createUser(userId);
        User blockedUser = createUser(2L);
        User writer = createUser(3L);
        QuestionBuy question = QuestionBuy.builder()
                .id(10L)
                .user(writer)
                .title("일간 인기 질문")
                .build();
        QuestionHomeResDto response = QuestionHomeResDto.builder()
                .qType("Buy")
                .id(question.getId())
                .title(question.getTitle())
                .build();

        when(userBlockDomainService.getAllBlockedUser(userId)).thenReturn(List.of(createUserBlock(user, blockedUser)));
        when(questionDomainService.getDailyHotQuestion(List.of(blockedUser.getId()))).thenReturn(List.of(question));
        when(questionResponseAssembler.getQuestionHomeResponse(question)).thenReturn(response);

        // when
        List<QuestionHomeResDto> result = questionRankService.getDailyHotQuestions(userId);

        // then
        assertThat(result).containsExactly(response);
    }

    @Test
    @DisplayName("주간 인기 질문을 조회한다.")
    void getWeeklyHotQuestionsTest() {
        // given
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        User user = createUser(userId);
        User blockedUser = createUser(2L);
        User writer = createUser(3L);
        QuestionRecommend question = QuestionRecommend.builder()
                .id(11L)
                .user(writer)
                .title("주간 인기 질문")
                .content("질문 내용")
                .build();
        QuestionSimpleResDto response = QuestionSimpleResDto.builder()
                .qType("Recommend")
                .id(question.getId())
                .title(question.getTitle())
                .build();

        when(userBlockDomainService.getAllBlockedUser(userId)).thenReturn(List.of(createUserBlock(user, blockedUser)));
        when(questionDomainService.getWeeklyHotQuestion(List.of(blockedUser.getId()), pageable))
                .thenReturn(new PageImpl<>(List.of(question), pageable, 1));
        when(questionResponseAssembler.getQuestionSimpleResponseWithImages(question)).thenReturn(response);

        // when
        PaginationResponse<QuestionSimpleResDto> result = questionRankService.getWeeklyHotQuestions(userId, pageable);

        // then
        assertThat(result.getContent()).containsExactly(response);
    }

    private User createUser(Long id) {
        return User.builder()
                .id(id)
                .email("user@test.com")
                .nickname("사용자")
                .snsType(SnsType.ETC)
                .build();
    }

    private UserBlock createUserBlock(User user, User blockedUser) {
        return UserBlock.builder()
                .user(user)
                .blockedUser(blockedUser)
                .build();
    }

}
