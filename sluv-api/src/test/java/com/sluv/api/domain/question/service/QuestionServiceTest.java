package com.sluv.api.domain.question.service;

import com.sluv.api.moderation.service.QuestionModerationService;
import com.sluv.api.question.dto.QuestionBuyPostReqDto;
import com.sluv.api.question.dto.QuestionFindPostReqDto;
import com.sluv.api.question.dto.QuestionHowaboutPostReqDto;
import com.sluv.api.question.dto.QuestionPostResDto;
import com.sluv.api.question.dto.QuestionRecommendPostReqDto;
import com.sluv.api.question.helper.QuestionImageManager;
import com.sluv.api.question.helper.QuestionItemManager;
import com.sluv.api.question.service.QuestionService;
import com.sluv.api.question.service.QuestionVoteService;
import com.sluv.domain.celeb.service.CelebDomainService;
import com.sluv.domain.celeb.service.NewCelebDomainService;
import com.sluv.domain.closet.service.ClosetDomainService;
import com.sluv.domain.comment.service.CommentDomainService;
import com.sluv.domain.question.entity.QuestionBuy;
import com.sluv.domain.question.entity.QuestionFind;
import com.sluv.domain.question.entity.QuestionHowabout;
import com.sluv.domain.question.entity.QuestionRecommend;
import com.sluv.domain.question.service.QuestionDomainService;
import com.sluv.domain.question.service.QuestionLikeDomainService;
import com.sluv.domain.question.service.QuestionRecommendCategoryDomainService;
import com.sluv.domain.question.service.QuestionVoteDomainService;
import com.sluv.domain.question.service.RecentQuestionDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private QuestionDomainService questionDomainService;

    @Mock
    private QuestionRecommendCategoryDomainService questionRecommendCategoryDomainService;

    @Mock
    private QuestionLikeDomainService questionLikeDomainService;

    @Mock
    private CommentDomainService commentDomainService;

    @Mock
    private CelebDomainService celebDomainService;

    @Mock
    private NewCelebDomainService newCelebDomainService;

    @Mock
    private RecentQuestionDomainService recentQuestionDomainService;

    @Mock
    private ClosetDomainService closetDomainService;

    @Mock
    private QuestionVoteDomainService questionVoteDomainService;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private QuestionImageManager questionImageManager;

    @Mock
    private QuestionItemManager questionItemManager;

    @Mock
    private QuestionVoteService questionVoteService;

    @Mock
    private QuestionModerationService questionModerationService;

    @Test
    @DisplayName("찾아주세요 질문 신규 등록 후 검수 작업 생성을 요청한다.")
    void postQuestionFindCreatesModerationJobTest() {
        // given
        Long userId = 10L;
        User user = createUser(userId);
        QuestionFindPostReqDto request = QuestionFindPostReqDto.builder()
                .title("찾아주세요")
                .content("질문 내용")
                .imgList(List.of())
                .itemList(List.of())
                .build();
        QuestionFind savedQuestion = QuestionFind.builder()
                .id(1L)
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        when(userDomainService.findById(userId)).thenReturn(user);
        when(questionDomainService.saveQuestion(org.mockito.ArgumentMatchers.any(QuestionFind.class)))
                .thenReturn(savedQuestion);

        // when
        QuestionPostResDto response = questionService.postQuestionFind(userId, request);

        // then
        assertThat(response.getId()).isEqualTo(savedQuestion.getId());
        verify(questionModerationService).createQuestionJobIfEnabled(savedQuestion);
    }

    @Test
    @DisplayName("찾아주세요 질문 수정 후 검수 작업 생성을 요청한다.")
    void updateQuestionFindCreatesModerationJobTest() {
        // given
        Long userId = 10L;
        User user = createUser(userId);
        QuestionFindPostReqDto request = QuestionFindPostReqDto.builder()
                .id(1L)
                .title("수정된 찾아주세요")
                .content("수정된 질문 내용")
                .imgList(List.of())
                .itemList(List.of())
                .build();
        QuestionFind savedQuestion = QuestionFind.builder()
                .id(request.getId())
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        when(userDomainService.findById(userId)).thenReturn(user);
        when(questionDomainService.saveQuestion(org.mockito.ArgumentMatchers.any(QuestionFind.class)))
                .thenReturn(savedQuestion);

        // when
        QuestionPostResDto response = questionService.postQuestionFind(userId, request);

        // then
        assertThat(response.getId()).isEqualTo(savedQuestion.getId());
        verify(questionModerationService).createQuestionJobIfEnabled(savedQuestion);
    }

    @Test
    @DisplayName("이 중에 뭐 살까 질문 신규 등록 후 검수 작업 생성을 요청한다.")
    void postQuestionBuyCreatesModerationJobTest() {
        // given
        Long userId = 10L;
        User user = createUser(userId);
        QuestionBuyPostReqDto request = QuestionBuyPostReqDto.builder()
                .title("뭐 살까")
                .voteEndTime(LocalDateTime.now().plusDays(1))
                .imgList(List.of())
                .itemList(List.of())
                .build();
        QuestionBuy savedQuestion = QuestionBuy.builder()
                .id(2L)
                .user(user)
                .title(request.getTitle())
                .voteEndTime(request.getVoteEndTime())
                .build();

        when(userDomainService.findById(userId)).thenReturn(user);
        when(questionDomainService.saveQuestion(org.mockito.ArgumentMatchers.any(QuestionBuy.class)))
                .thenReturn(savedQuestion);

        // when
        QuestionPostResDto response = questionService.postQuestionBuy(userId, request);

        // then
        assertThat(response.getId()).isEqualTo(savedQuestion.getId());
        verify(questionModerationService).createQuestionJobIfEnabled(savedQuestion);
    }

    @Test
    @DisplayName("이거 어때 질문 신규 등록 후 검수 작업 생성을 요청한다.")
    void postQuestionHowaboutCreatesModerationJobTest() {
        // given
        Long userId = 10L;
        User user = createUser(userId);
        QuestionHowaboutPostReqDto request = QuestionHowaboutPostReqDto.builder()
                .title("이거 어때")
                .content("질문 내용")
                .imgList(List.of())
                .itemList(List.of())
                .build();
        QuestionHowabout savedQuestion = QuestionHowabout.builder()
                .id(3L)
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        when(userDomainService.findById(userId)).thenReturn(user);
        when(questionDomainService.saveQuestion(org.mockito.ArgumentMatchers.any(QuestionHowabout.class)))
                .thenReturn(savedQuestion);

        // when
        QuestionPostResDto response = questionService.postQuestionHowabout(userId, request);

        // then
        assertThat(response.getId()).isEqualTo(savedQuestion.getId());
        verify(questionModerationService).createQuestionJobIfEnabled(savedQuestion);
    }

    @Test
    @DisplayName("추천해줘 질문 신규 등록 후 검수 작업 생성을 요청한다.")
    void postQuestionRecommendCreatesModerationJobTest() {
        // given
        Long userId = 10L;
        User user = createUser(userId);
        QuestionRecommendPostReqDto request = QuestionRecommendPostReqDto.builder()
                .title("추천해줘")
                .content("질문 내용")
                .categoryNameList(List.of("상의"))
                .imgList(List.of())
                .itemList(List.of())
                .build();
        QuestionRecommend savedQuestion = QuestionRecommend.builder()
                .id(4L)
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        when(userDomainService.findById(userId)).thenReturn(user);
        when(questionDomainService.saveQuestion(org.mockito.ArgumentMatchers.any(QuestionRecommend.class)))
                .thenReturn(savedQuestion);

        // when
        QuestionPostResDto response = questionService.postQuestionRecommend(userId, request);

        // then
        assertThat(response.getId()).isEqualTo(savedQuestion.getId());
        verify(questionModerationService).createQuestionJobIfEnabled(savedQuestion);
    }

    private User createUser(Long id) {
        return User.builder()
                .id(id)
                .email("user" + id + "@example.com")
                .build();
    }
}
