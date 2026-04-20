package com.sluv.api.question.helper;

import com.sluv.api.question.dto.QuestionHomeResDto;
import com.sluv.domain.comment.repository.CommentRepository;
import com.sluv.domain.item.entity.ItemImg;
import com.sluv.domain.item.repository.ItemImgRepository;
import com.sluv.domain.question.dto.QuestionImgSimpleDto;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.QuestionBuy;
import com.sluv.domain.question.entity.QuestionFind;
import com.sluv.domain.question.entity.QuestionHowabout;
import com.sluv.domain.question.entity.QuestionImg;
import com.sluv.domain.question.entity.QuestionItem;
import com.sluv.domain.question.entity.QuestionRecommend;
import com.sluv.domain.question.entity.QuestionRecommendCategory;
import com.sluv.domain.question.exception.QuestionTypeNotFoundException;
import com.sluv.domain.question.repository.QuestionImgRepository;
import com.sluv.domain.question.repository.QuestionItemRepository;
import com.sluv.domain.question.repository.QuestionLikeRepository;
import com.sluv.domain.question.repository.QuestionRecommendCategoryRepository;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionResponseHelper {

    private final ItemImgRepository itemImgRepository;
    private final QuestionImgRepository questionImgRepository;
    private final QuestionItemRepository questionItemRepository;
    private final QuestionLikeRepository questionLikeRepository;
    private final QuestionRecommendCategoryRepository questionRecommendCategoryRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public QuestionSimpleResDto getQuestionSimpleResponse(Question question) {
        validateQuestionType(question);

        List<QuestionImgSimpleDto> questionImages = getQuestionImages(question);
        List<QuestionImgSimpleDto> itemMainImages = getBuyItemMainImages(question);
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

        List<QuestionImgSimpleDto> questionImages = getQuestionImagesWithMainImage(question);
        List<QuestionImgSimpleDto> itemMainImages = getItemMainImagesForCard(question);
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

        List<QuestionImgSimpleDto> questionImages = getQuestionImagesForHome(question);
        User writer = userRepository.findById(question.getUser().getId()).orElse(null);

        return QuestionHomeResDto.of(question, writer, questionImages);
    }

    public QuestionSimpleResDto getQuestionSimpleResponseWithImages(Question question) {
        validateQuestionType(question);

        List<QuestionImgSimpleDto> questionImages = getAllQuestionImages(question);
        List<QuestionImgSimpleDto> itemMainImages = getAllItemMainImages(question);
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

    private List<QuestionImgSimpleDto> getQuestionImages(Question question) {
        if (!(question instanceof QuestionBuy)) {
            return null;
        }

        return getAllQuestionImages(question);
    }

    private List<QuestionImgSimpleDto> getAllQuestionImages(Question question) {
        return questionImgRepository.findAllByQuestionId(question.getId()).stream()
                .map(QuestionImgSimpleDto::of)
                .toList();
    }

    private List<QuestionImgSimpleDto> getQuestionImagesWithMainImage(Question question) {
        if (question instanceof QuestionBuy) {
            return getQuestionImages(question);
        }

        List<QuestionImgSimpleDto> questionImages = new ArrayList<>();
        QuestionImg mainQuestionImage = questionImgRepository.findByQuestionIdAndRepresentFlag(question.getId(), true);
        if (mainQuestionImage != null) {
            questionImages.add(QuestionImgSimpleDto.of(mainQuestionImage));
        }

        QuestionImgSimpleDto itemMainImage = getItemMainImage(question);
        if (itemMainImage != null) {
            questionImages.add(itemMainImage);
        }

        return questionImages;
    }

    private List<QuestionImgSimpleDto> getBuyItemMainImages(Question question) {
        if (!(question instanceof QuestionBuy)) {
            return null;
        }

        return getAllItemMainImages(question);
    }

    private List<QuestionImgSimpleDto> getAllItemMainImages(Question question) {
        return questionItemRepository.findAllByQuestionId(question.getId()).stream()
                .map(questionItem -> {
                    ItemImg mainImg = itemImgRepository.findMainImg(questionItem.getItem().getId());
                    return QuestionImgSimpleDto.of(mainImg);
                })
                .toList();
    }

    private List<QuestionImgSimpleDto> getItemMainImagesForCard(Question question) {
        if (!(question instanceof QuestionBuy)) {
            return new ArrayList<>();
        }

        return getBuyItemMainImages(question);
    }

    private QuestionImgSimpleDto getItemMainImage(Question question) {
        QuestionItem mainQuestionItem = questionItemRepository.findByQuestionIdAndRepresentFlag(question.getId(), true);
        if (mainQuestionItem == null) {
            return null;
        }

        ItemImg mainItemImage = itemImgRepository.findMainImg(mainQuestionItem.getItem().getId());
        return QuestionImgSimpleDto.of(mainItemImage);
    }

    private List<QuestionImgSimpleDto> getQuestionImagesForHome(Question question) {
        if (question instanceof QuestionBuy) {
            List<QuestionImgSimpleDto> questionImages = new ArrayList<>();
            questionImages.addAll(getQuestionImages(question));
            questionImages.addAll(getBuyItemMainImages(question));
            questionImages.sort(Comparator.comparing(QuestionImgSimpleDto::getSortOrder));

            return questionImages;
        }

        QuestionImg mainQuestionImage = questionImgRepository.findByQuestionIdAndRepresentFlag(question.getId(), true);
        QuestionItem mainQuestionItem = questionItemRepository.findByQuestionIdAndRepresentFlag(question.getId(), true);
        String imageUrl = null;

        if (mainQuestionImage != null) {
            imageUrl = mainQuestionImage.getImgUrl();
        } else if (mainQuestionItem != null) {
            imageUrl = itemImgRepository.findMainImg(mainQuestionItem.getItem().getId()).getItemImgUrl();
        }

        return imageUrl != null
                ? Arrays.asList(new QuestionImgSimpleDto(imageUrl, 0L))
                : null;
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
