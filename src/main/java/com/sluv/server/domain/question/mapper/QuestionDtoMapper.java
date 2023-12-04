package com.sluv.server.domain.question.mapper;

import com.sluv.server.domain.comment.repository.CommentRepository;
import com.sluv.server.domain.item.entity.ItemImg;
import com.sluv.server.domain.item.repository.ItemImgRepository;
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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionDtoMapper {

    private final ItemImgRepository itemImgRepository;
    private final QuestionImgRepository questionImgRepository;
    private final QuestionItemRepository questionItemRepository;
    private final QuestionLikeRepository questionLikeRepository;
    private final QuestionRecommendCategoryRepository questionRecommendCategoryRepository;
    private final CommentRepository commentRepository;

    public QuestionSimpleResDto dtoBuildByQuestionType(Question question) {
        String qType = null;
        List<QuestionImgSimpleResDto> imgList = null;
        List<QuestionImgSimpleResDto> itemImgList = null;
        String celebName = null;
        List<String> categoryList = null;

        if (question instanceof QuestionBuy) { // 1. question이 QuestionBuy 일 경우
            // 이미지 DTO 생성
            List<QuestionImgSimpleResDto> imgSimpleList = questionImgRepository.findAllByQuestionId(question.getId())
                    .stream().map(QuestionImgSimpleResDto::of).toList();
            // 아이템 이미지 DTO 생성
            List<QuestionImgSimpleResDto> itemImgSimpleList = questionItemRepository.findAllByQuestionId(
                    question.getId()).stream().map(questionItem -> {
                ItemImg mainImg = itemImgRepository.findMainImg(questionItem.getItem().getId());
                return QuestionImgSimpleResDto.of(mainImg);
            }).toList();

            qType = "Buy";
            imgList = imgSimpleList;
            itemImgList = itemImgSimpleList;

        } else if (question instanceof QuestionFind questionFind) { // 2. question이 QuestionFind 일 경우
            qType = "Find";
            celebName = questionFind.getCeleb() != null ? questionFind.getCeleb().getParent() != null ?
                    questionFind.getCeleb().getParent().getCelebNameKr() + " " + questionFind.getCeleb()
                            .getCelebNameKr() : questionFind.getCeleb().getCelebNameKr()
                    : questionFind.getNewCeleb().getCelebName();

        } else if (question instanceof QuestionHowabout) { // 3. question이 QuestionHowabout 일 경우
            qType = "How";

        } else if (question instanceof QuestionRecommend) { // 4. question이 QuestionRecommend 일 경우
            List<String> categoryNameList = questionRecommendCategoryRepository.findAllByQuestionId(question.getId())
                    .stream().map(QuestionRecommendCategory::getName).toList();
            qType = "Recommend";
            categoryList = categoryNameList;

        } else {
            throw new QuestionTypeNotFoundException();
        }

        // Question 좋아요 수
        Long likeNum = questionLikeRepository.countByQuestionId(question.getId());

        // Question 댓글 수
        Long commentNum = commentRepository.countByQuestionId(question.getId());

        return QuestionSimpleResDto.of(question, likeNum, commentNum, imgList, itemImgList, categoryList);
    }
}
