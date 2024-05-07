package com.sluv.server.domain.user.service;

import com.sluv.server.domain.comment.repository.CommentRepository;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.ItemImg;
import com.sluv.server.domain.item.repository.ItemImgRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.question.dto.QuestionImgSimpleResDto;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.question.entity.QuestionBuy;
import com.sluv.server.domain.question.entity.QuestionFind;
import com.sluv.server.domain.question.entity.QuestionHowabout;
import com.sluv.server.domain.question.entity.QuestionRecommend;
import com.sluv.server.domain.question.entity.QuestionRecommendCategory;
import com.sluv.server.domain.question.exception.QuestionTypeNotFoundException;
import com.sluv.server.domain.question.repository.QuestionImgRepository;
import com.sluv.server.domain.question.repository.QuestionItemRepository;
import com.sluv.server.domain.question.repository.QuestionLikeRepository;
import com.sluv.server.domain.question.repository.QuestionRecommendCategoryRepository;
import com.sluv.server.domain.question.repository.RecentQuestionRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.common.response.PaginationCountResDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRecentService {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final QuestionImgRepository questionImgRepository;
    private final QuestionItemRepository questionItemRepository;
    private final QuestionLikeRepository questionLikeRepository;
    private final RecentQuestionRepository recentQuestionRepository;
    private final QuestionRecommendCategoryRepository questionRecommendCategoryRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public PaginationCountResDto<ItemSimpleResDto> getUserRecentItem(User user, Pageable pageable) {

        Page<Item> recentItemPage = itemRepository.getUserAllRecentItem(user, pageable);

        List<ItemSimpleResDto> content = itemRepository.getItemSimpleResDto(user, recentItemPage.getContent());

        return new PaginationCountResDto<>(recentItemPage.hasNext(), recentItemPage.getNumber(), content,
                recentItemPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public PaginationCountResDto<QuestionSimpleResDto> getUserRecentQuestion(User user, Pageable pageable) {

        Page<Question> recentQuestionPage = recentQuestionRepository.getUserAllRecentQuestion(user, pageable);

        List<QuestionSimpleResDto> content = recentQuestionPage.stream().map(question -> {
            List<QuestionImgSimpleResDto> imgList = null;
            List<QuestionImgSimpleResDto> itemImgList = null;
            List<String> categoryList = null;

            if (question instanceof QuestionBuy) {
                // 이미지 Dto 생성
                imgList = questionImgRepository.findAllByQuestionId(question.getId()).stream()
                        .map(QuestionImgSimpleResDto::of).toList();
                // 아이템 이미지 Dto 생성
                itemImgList = questionItemRepository.findAllByQuestionId(question.getId()).stream()
                        .map(questionItem -> {
                            ItemImg mainImg = itemImgRepository.findMainImg(questionItem.getItem().getId());
                            return QuestionImgSimpleResDto.of(mainImg);
                        }).toList();

            } else if (question instanceof QuestionHowabout) {

            } else if (question instanceof QuestionRecommend) {
                // Question 카테고리
                categoryList = questionRecommendCategoryRepository.findAllByQuestionId(question.getId()).stream()
                        .map(QuestionRecommendCategory::getName).toList();
            } else if (question instanceof QuestionFind) {

            } else {
                throw new QuestionTypeNotFoundException();
            }

            // Question 좋아요 수
            Long likeNum = questionLikeRepository.countByQuestionId(question.getId());

            // Question 댓글 수
            Long commentNum = commentRepository.countByQuestionId(question.getId());

            User writer = userRepository.findById(question.getUser().getId()).orElse(null);

            return QuestionSimpleResDto.of(question, writer, likeNum, commentNum, imgList, itemImgList, categoryList);


        }).toList();

        return new PaginationCountResDto<>(recentQuestionPage.hasNext(), recentQuestionPage.getNumber(), content,
                recentQuestionPage.getTotalElements());
    }
}
