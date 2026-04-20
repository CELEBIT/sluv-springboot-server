package com.sluv.api.domain.question.helper;

import com.sluv.api.question.helper.QuestionResponseHelper;
import com.sluv.domain.auth.enums.SnsType;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.comment.repository.CommentRepository;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemImg;
import com.sluv.domain.item.repository.ItemImgRepository;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.entity.QuestionBuy;
import com.sluv.domain.question.entity.QuestionFind;
import com.sluv.domain.question.entity.QuestionHowabout;
import com.sluv.domain.question.entity.QuestionImg;
import com.sluv.domain.question.entity.QuestionItem;
import com.sluv.domain.question.entity.QuestionRecommend;
import com.sluv.domain.question.entity.QuestionRecommendCategory;
import com.sluv.domain.question.repository.QuestionImgRepository;
import com.sluv.domain.question.repository.QuestionItemRepository;
import com.sluv.domain.question.repository.QuestionLikeRepository;
import com.sluv.domain.question.repository.QuestionRecommendCategoryRepository;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
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
public class QuestionResponseHelperTest {

    @InjectMocks
    private QuestionResponseHelper questionResponseHelper;

    @Mock
    private ItemImgRepository itemImgRepository;

    @Mock
    private QuestionImgRepository questionImgRepository;

    @Mock
    private QuestionItemRepository questionItemRepository;

    @Mock
    private QuestionLikeRepository questionLikeRepository;

    @Mock
    private QuestionRecommendCategoryRepository questionRecommendCategoryRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("Buy 질문 응답에 질문 이미지와 아이템 대표 이미지를 포함한다.")
    void getQuestionSimpleResponseWithBuyTest() {
        // given
        User writer = createUser(10L);
        QuestionBuy question = QuestionBuy.builder()
                .id(1L)
                .user(writer)
                .title("질문 제목")
                .content("질문 내용")
                .build();
        QuestionImg questionImg = QuestionImg.builder()
                .question(question)
                .imgUrl("https://question-image.test/1.jpg")
                .sortOrder(1)
                .representFlag(true)
                .build();
        Item item = createItem(20L);
        QuestionItem questionItem = QuestionItem.builder()
                .question(question)
                .item(item)
                .sortOrder(1)
                .representFlag(true)
                .build();
        ItemImg itemImg = ItemImg.builder()
                .item(item)
                .itemImgUrl("https://item-image.test/1.jpg")
                .sortOrder(1)
                .representFlag(true)
                .build();

        when(questionImgRepository.findAllByQuestionId(question.getId())).thenReturn(List.of(questionImg));
        when(questionItemRepository.findAllByQuestionId(question.getId())).thenReturn(List.of(questionItem));
        when(itemImgRepository.findMainImg(item.getId())).thenReturn(itemImg);
        when(questionLikeRepository.countByQuestionId(question.getId())).thenReturn(2L);
        when(commentRepository.countByQuestionId(question.getId())).thenReturn(3L);
        when(userRepository.findById(writer.getId())).thenReturn(Optional.of(writer));

        // when
        QuestionSimpleResDto response = questionResponseHelper.getQuestionSimpleResponse(question);

        // then
        assertThat(response.getQType()).isEqualTo("Buy");
        assertThat(response.getId()).isEqualTo(question.getId());
        assertThat(response.getUser().getId()).isEqualTo(writer.getId());
        assertThat(response.getLikeNum()).isEqualTo(2L);
        assertThat(response.getCommentNum()).isEqualTo(3L);
        assertThat(response.getImgList()).hasSize(1);
        assertThat(response.getImgList().get(0).getImgUrl()).isEqualTo("https://question-image.test/1.jpg");
        assertThat(response.getItemImgList()).hasSize(1);
        assertThat(response.getItemImgList().get(0).getImgUrl()).isEqualTo("https://item-image.test/1.jpg");
    }

    @Test
    @DisplayName("Find 질문 응답에 셀럽 이름을 포함하고 이미지/카테고리는 조회하지 않는다.")
    void getQuestionSimpleResponseWithFindTest() {
        // given
        User writer = createUser(10L);
        Celeb parentCeleb = createCeleb(1L, null, "부모 셀럽");
        Celeb childCeleb = createCeleb(2L, parentCeleb, "자식 셀럽");
        QuestionFind question = QuestionFind.builder()
                .id(2L)
                .user(writer)
                .title("질문 제목")
                .content("질문 내용")
                .celeb(childCeleb)
                .build();

        when(questionLikeRepository.countByQuestionId(question.getId())).thenReturn(1L);
        when(commentRepository.countByQuestionId(question.getId())).thenReturn(4L);
        when(userRepository.findById(writer.getId())).thenReturn(Optional.of(writer));

        // when
        QuestionSimpleResDto response = questionResponseHelper.getQuestionSimpleResponse(question);

        // then
        assertThat(response.getQType()).isEqualTo("Find");
        assertThat(response.getCelebName()).isEqualTo("부모 셀럽 자식 셀럽");
        assertThat(response.getImgList()).isNull();
        assertThat(response.getItemImgList()).isNull();
        assertThat(response.getCategoryName()).isNull();
        verify(questionImgRepository, never()).findAllByQuestionId(question.getId());
        verify(questionItemRepository, never()).findAllByQuestionId(question.getId());
        verify(questionRecommendCategoryRepository, never()).findAllByQuestionId(question.getId());
    }

    @Test
    @DisplayName("How 질문 응답은 공통 응답 값만 포함한다.")
    void getQuestionSimpleResponseWithHowTest() {
        // given
        User writer = createUser(10L);
        QuestionHowabout question = QuestionHowabout.builder()
                .id(3L)
                .user(writer)
                .title("질문 제목")
                .content("질문 내용")
                .build();

        when(questionLikeRepository.countByQuestionId(question.getId())).thenReturn(5L);
        when(commentRepository.countByQuestionId(question.getId())).thenReturn(6L);
        when(userRepository.findById(writer.getId())).thenReturn(Optional.of(writer));

        // when
        QuestionSimpleResDto response = questionResponseHelper.getQuestionSimpleResponse(question);

        // then
        assertThat(response.getQType()).isEqualTo("How");
        assertThat(response.getLikeNum()).isEqualTo(5L);
        assertThat(response.getCommentNum()).isEqualTo(6L);
        assertThat(response.getImgList()).isNull();
        assertThat(response.getItemImgList()).isNull();
        assertThat(response.getCategoryName()).isNull();
        verify(questionImgRepository, never()).findAllByQuestionId(question.getId());
        verify(questionItemRepository, never()).findAllByQuestionId(question.getId());
        verify(questionRecommendCategoryRepository, never()).findAllByQuestionId(question.getId());
    }

    @Test
    @DisplayName("Recommend 질문 응답에 카테고리 이름 목록을 포함한다.")
    void getQuestionSimpleResponseWithRecommendTest() {
        // given
        User writer = createUser(10L);
        QuestionRecommend question = QuestionRecommend.builder()
                .id(4L)
                .user(writer)
                .title("질문 제목")
                .content("질문 내용")
                .build();
        List<QuestionRecommendCategory> categories = List.of(
                QuestionRecommendCategory.toEntity(question, "상의"),
                QuestionRecommendCategory.toEntity(question, "아우터")
        );

        when(questionRecommendCategoryRepository.findAllByQuestionId(question.getId())).thenReturn(categories);
        when(questionLikeRepository.countByQuestionId(question.getId())).thenReturn(7L);
        when(commentRepository.countByQuestionId(question.getId())).thenReturn(8L);
        when(userRepository.findById(writer.getId())).thenReturn(Optional.of(writer));

        // when
        QuestionSimpleResDto response = questionResponseHelper.getQuestionSimpleResponse(question);

        // then
        assertThat(response.getQType()).isEqualTo("Recommend");
        assertThat(response.getCategoryName()).containsExactly("상의", "아우터");
        assertThat(response.getImgList()).isNull();
        assertThat(response.getItemImgList()).isNull();
        verify(questionImgRepository, never()).findAllByQuestionId(question.getId());
        verify(questionItemRepository, never()).findAllByQuestionId(question.getId());
    }

    private User createUser(Long id) {
        return User.builder()
                .id(id)
                .email("test@sluv.com")
                .nickname("작성자")
                .snsType(SnsType.ETC)
                .profileImgUrl("https://profile.test/profile.jpg")
                .build();
    }

    private Item createItem(Long id) {
        return Item.builder()
                .id(id)
                .name("아이템")
                .price(10000)
                .build();
    }

    private Celeb createCeleb(Long id, Celeb parent, String name) {
        return Celeb.builder()
                .id(id)
                .parent(parent)
                .celebNameKr(name)
                .celebNameEn(name)
                .build();
    }
}
