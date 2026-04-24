package com.sluv.api.question.helper;

import com.sluv.api.question.dto.QuestionHomeResDto;
import com.sluv.domain.comment.repository.CommentRepository;
import com.sluv.domain.question.dto.QuestionImgSimpleDto;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.QuestionBuy;
import com.sluv.domain.question.entity.QuestionFind;
import com.sluv.domain.question.entity.QuestionHowabout;
import com.sluv.domain.question.entity.QuestionRecommend;
import com.sluv.domain.question.entity.QuestionRecommendCategory;
import com.sluv.domain.question.exception.QuestionTypeNotFoundException;
import com.sluv.domain.question.repository.QuestionLikeRepository;
import com.sluv.domain.question.repository.QuestionRecommendCategoryRepository;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionResponseAssembler {

    private final QuestionImageManager questionImageManager;
    private final QuestionLikeRepository questionLikeRepository;
    private final QuestionRecommendCategoryRepository questionRecommendCategoryRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public QuestionSimpleResDto getQuestionSimpleResponse(Question question) {
        validateQuestionType(question);

        List<QuestionImgSimpleDto> questionImages = questionImageManager.getQuestionImages(question);
        List<QuestionImgSimpleDto> itemMainImages = questionImageManager.getBuyItemMainImages(question);
        List<String> recommendCategoryNames = getRecommendCategoryNames(question);
        Long likeNum = questionLikeRepository.countByQuestionId(question.getId());
        Long commentNum = commentRepository.countByQuestionId(question.getId());
        User writer = userRepository.findById(question.getUser().getId()).orElse(null);

        return QuestionSimpleResDto.of(
                question,
                writer,
                likeNum,
                commentNum,
                questionImages,
                itemMainImages,
                recommendCategoryNames
        );
    }

    public QuestionSimpleResDto getQuestionSimpleResponseWithMainImage(Question question) {
        validateQuestionType(question);

        List<QuestionImgSimpleDto> questionImages = questionImageManager.getQuestionImagesWithMainImage(question);
        List<QuestionImgSimpleDto> itemMainImages = questionImageManager.getItemMainImagesForCard(question);
        List<String> recommendCategoryNames = getRecommendCategoryNames(question);
        Long likeNum = questionLikeRepository.countByQuestionId(question.getId());
        Long commentNum = commentRepository.countByQuestionId(question.getId());
        User writer = userRepository.findById(question.getUser().getId()).orElse(null);

        return QuestionSimpleResDto.of(
                question,
                writer,
                likeNum,
                commentNum,
                questionImages,
                itemMainImages,
                recommendCategoryNames
        );
    }

    public QuestionHomeResDto getQuestionHomeResponse(Question question) {
        validateQuestionType(question);

        List<QuestionImgSimpleDto> questionImages = questionImageManager.getQuestionImagesForHome(question);
        User writer = userRepository.findById(question.getUser().getId()).orElse(null);

        return QuestionHomeResDto.of(question, writer, questionImages);
    }

    public QuestionSimpleResDto getQuestionSimpleResponseWithImages(Question question) {
        validateQuestionType(question);

        List<QuestionImgSimpleDto> questionImages = questionImageManager.getAllQuestionImages(question);
        List<QuestionImgSimpleDto> itemMainImages = questionImageManager.getAllItemMainImages(question);
        List<String> recommendCategoryNames = getRecommendCategoryNames(question);
        Long likeNum = questionLikeRepository.countByQuestionId(question.getId());
        Long commentNum = commentRepository.countByQuestionId(question.getId());
        User writer = userRepository.findById(question.getUser().getId()).orElse(null);

        return QuestionSimpleResDto.of(
                question,
                writer,
                likeNum,
                commentNum,
                questionImages,
                itemMainImages,
                recommendCategoryNames
        );
    }

    private void validateQuestionType(Question question) {
        if (question instanceof QuestionBuy
                || question instanceof QuestionFind
                || question instanceof QuestionHowabout
                || question instanceof QuestionRecommend) {
            return;
        }

        throw new QuestionTypeNotFoundException();
    }


    private List<String> getRecommendCategoryNames(Question question) {
        if (!(question instanceof QuestionRecommend)) {
            return null;
        }

        return questionRecommendCategoryRepository.findAllByQuestionId(question.getId()).stream()
                .map(QuestionRecommendCategory::getName)
                .toList();
    }
}
