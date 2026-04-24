package com.sluv.domain.question.entity;

import com.sluv.domain.question.enums.QuestionStatus;
import com.sluv.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionStatusTest {

    @Test
    @DisplayName("질문 상태에 PENDING이 존재한다.")
    void questionStatusHasPendingTest() {
        // when
        QuestionStatus status = QuestionStatus.valueOf("PENDING");

        // then
        assertThat(status.name()).isEqualTo("PENDING");
    }

    @Test
    @DisplayName("찾아주세요 질문 생성 시 상태는 ACTIVE다.")
    void questionFindDefaultStatusIsActiveTest() {
        // given
        User user = createUser();

        // when
        QuestionFind question = QuestionFind.toEntity(user, null, "찾아주세요", "내용", null, null);

        // then
        assertThat(question.getQuestionStatus()).isEqualTo(QuestionStatus.ACTIVE);
    }

    @Test
    @DisplayName("이 중에 뭐 살까 질문 생성 시 상태는 ACTIVE다.")
    void questionBuyDefaultStatusIsActiveTest() {
        // given
        User user = createUser();

        // when
        QuestionBuy question = QuestionBuy.toEntity(user, null, "뭐 살까", LocalDateTime.now().plusDays(1));

        // then
        assertThat(question.getQuestionStatus()).isEqualTo(QuestionStatus.ACTIVE);
    }

    @Test
    @DisplayName("이거 어때 질문 생성 시 상태는 ACTIVE다.")
    void questionHowaboutDefaultStatusIsActiveTest() {
        // given
        User user = createUser();

        // when
        QuestionHowabout question = QuestionHowabout.toEntity(user, null, "이거 어때", "내용");

        // then
        assertThat(question.getQuestionStatus()).isEqualTo(QuestionStatus.ACTIVE);
    }

    @Test
    @DisplayName("추천해줘 질문 생성 시 상태는 ACTIVE다.")
    void questionRecommendDefaultStatusIsActiveTest() {
        // given
        User user = createUser();

        // when
        QuestionRecommend question = QuestionRecommend.toEntity(user, null, "추천해줘", "내용");

        // then
        assertThat(question.getQuestionStatus()).isEqualTo(QuestionStatus.ACTIVE);
    }

    private User createUser() {
        return User.builder()
                .id(1L)
                .email("user@example.com")
                .build();
    }
}
