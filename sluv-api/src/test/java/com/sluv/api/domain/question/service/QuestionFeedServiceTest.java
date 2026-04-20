package com.sluv.api.domain.question.service;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.question.dto.QuestionBuySimpleResDto;
import com.sluv.api.question.dto.QuestionVoteDataDto;
import com.sluv.api.question.helper.QuestionResponseHelper;
import com.sluv.api.question.service.QuestionFeedService;
import com.sluv.api.question.service.QuestionVoteService;
import com.sluv.domain.auth.enums.SnsType;
import com.sluv.domain.brand.entity.NewBrand;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemImg;
import com.sluv.domain.item.service.ItemImgDomainService;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.QuestionBuy;
import com.sluv.domain.question.entity.QuestionFind;
import com.sluv.domain.question.entity.QuestionHowabout;
import com.sluv.domain.question.entity.QuestionImg;
import com.sluv.domain.question.entity.QuestionItem;
import com.sluv.domain.question.entity.QuestionRecommend;
import com.sluv.domain.question.entity.QuestionVote;
import com.sluv.domain.question.service.QuestionDomainService;
import com.sluv.domain.question.service.QuestionImgDomainService;
import com.sluv.domain.question.service.QuestionItemDomainService;
import com.sluv.domain.question.service.QuestionVoteDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.entity.UserBlock;
import com.sluv.domain.user.service.UserBlockDomainService;
import com.sluv.domain.user.service.UserDomainService;
import java.time.LocalDateTime;
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

    @Mock
    private QuestionImgDomainService questionImgDomainService;

    @Mock
    private QuestionItemDomainService questionItemDomainService;

    @Mock
    private ItemImgDomainService itemImgDomainService;

    @Mock
    private QuestionVoteDomainService questionVoteDomainService;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private QuestionVoteService questionVoteService;

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
        when(questionResponseHelper.getQuestionSimpleResponseWithMainImage(question)).thenReturn(response);

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
        when(questionResponseHelper.getQuestionSimpleResponseWithMainImage(question)).thenReturn(response);

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
        when(questionResponseHelper.getQuestionSimpleResponseWithMainImage(question)).thenReturn(response);

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
        when(questionResponseHelper.getQuestionSimpleResponseWithMainImage(question)).thenReturn(response);

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
        when(questionResponseHelper.getQuestionSimpleResponseWithMainImage(question)).thenReturn(response);

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

    @Test
    @DisplayName("Buy 질문 피드를 조회한다.")
    void getBuyQuestionsTest() {
        // given
        Long userId = 1L;
        String voteStatus = "all";
        Pageable pageable = PageRequest.of(0, 10);
        User user = createUser(userId);
        User writer = createUser(2L);
        User blockedUser = createUser(3L);
        UserBlock userBlock = createUserBlock(user, blockedUser);
        LocalDateTime voteEndTime = LocalDateTime.of(2026, 4, 20, 12, 0);
        QuestionBuy question = QuestionBuy.builder()
                .id(15L)
                .user(writer)
                .title("Buy 질문")
                .content("질문 내용")
                .voteEndTime(voteEndTime)
                .build();
        QuestionImg questionImg = QuestionImg.builder()
                .question(question)
                .imgUrl("https://question-image.test/buy.jpg")
                .sortOrder(1)
                .representFlag(true)
                .build();
        Item item = createItem(20L);
        QuestionItem questionItem = QuestionItem.builder()
                .question(question)
                .item(item)
                .sortOrder(2)
                .representFlag(true)
                .build();
        ItemImg itemImg = ItemImg.builder()
                .item(item)
                .itemImgUrl("https://item-image.test/buy.jpg")
                .sortOrder(1)
                .representFlag(true)
                .build();
        QuestionVote questionVote = QuestionVote.builder()
                .question(question)
                .user(user)
                .voteSortOrder(2L)
                .build();
        Page<QuestionBuy> questions = new PageImpl<>(List.of(question), pageable, 1);

        when(userDomainService.findById(userId)).thenReturn(user);
        when(userBlockDomainService.getAllBlockedUser(userId)).thenReturn(List.of(userBlock));
        when(questionDomainService.getQuestionBuyList(voteStatus, List.of(blockedUser.getId()), pageable))
                .thenReturn(questions);
        when(questionImgDomainService.findAllByQuestionId(question.getId())).thenReturn(List.of(questionImg));
        when(questionItemDomainService.findAllByQuestionId(question.getId())).thenReturn(List.of(questionItem));
        when(itemImgDomainService.findMainImg(item.getId())).thenReturn(itemImg);
        when(questionVoteService.getVoteData(question.getId(), 1L)).thenReturn(QuestionVoteDataDto.of(4L, 40.0));
        when(questionVoteService.getVoteData(question.getId(), 2L)).thenReturn(QuestionVoteDataDto.of(6L, 60.0));
        when(questionVoteService.getTotalVoteCount(
                org.mockito.ArgumentMatchers.anyList(),
                org.mockito.ArgumentMatchers.anyList()
        )).thenReturn(10L);
        when(questionVoteDomainService.findByQuestionIdAndUserIdOrNull(question.getId(), user.getId()))
                .thenReturn(questionVote);
        when(userDomainService.findByIdOrNull(writer.getId())).thenReturn(writer);

        // when
        PaginationResponse<QuestionBuySimpleResDto> result = questionFeedService.getBuyQuestions(
                userId,
                voteStatus,
                pageable
        );

        // then
        assertThat(result.getContent()).hasSize(1);
        QuestionBuySimpleResDto response = result.getContent().get(0);
        assertThat(response.getQType()).isEqualTo("Buy");
        assertThat(response.getVoteNum()).isEqualTo(10L);
        assertThat(response.getVoteStatus()).isTrue();
        assertThat(response.getSelectedVoteNum()).isEqualTo(2L);
        assertThat(response.getImgList()).hasSize(1);
        assertThat(response.getItemImgList()).hasSize(1);
        verify(questionDomainService).getQuestionBuyList(voteStatus, List.of(blockedUser.getId()), pageable);
    }

    private User createUser(Long id) {
        return User.builder()
                .id(id)
                .email("user@test.com")
                .snsType(SnsType.ETC)
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

    private Item createItem(Long id) {
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
                .name("아이템")
                .price(10000)
                .build();
    }
}
