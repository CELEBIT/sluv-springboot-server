package com.sluv.api.question.helper;

import com.sluv.api.question.dto.QuestionImgReqDto;
import com.sluv.api.question.dto.QuestionImgResDto;
import com.sluv.api.question.dto.QuestionVoteDataDto;
import com.sluv.domain.item.entity.ItemImg;
import com.sluv.domain.item.repository.ItemImgRepository;
import com.sluv.domain.question.dto.QuestionImgSimpleDto;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.QuestionBuy;
import com.sluv.domain.question.entity.QuestionImg;
import com.sluv.domain.question.entity.QuestionItem;
import com.sluv.domain.question.repository.QuestionImgRepository;
import com.sluv.domain.question.repository.QuestionItemRepository;
import com.sluv.domain.question.service.QuestionImgDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class QuestionImageManager {

    private final QuestionImgDomainService questionImgDomainService;
    private final QuestionImgRepository questionImgRepository;
    private final QuestionItemRepository questionItemRepository;
    private final ItemImgRepository itemImgRepository;

    public void saveImages(List<QuestionImgReqDto> imageRequests, Question question) {
        questionImgDomainService.deleteAllByQuestionId(question.getId());

        if (imageRequests != null) {
            List<QuestionImg> questionImages = imageRequests.stream()
                    .map(imageRequest -> QuestionImg.toEntity(
                            question,
                            imageRequest.getImgUrl(),
                            imageRequest.getDescription(),
                            imageRequest.getRepresentFlag(),
                            imageRequest.getSortOrder()
                    ))
                    .toList();

            questionImgDomainService.saveAll(questionImages);
        }
    }

    public List<QuestionImgResDto> getQuestionImageResponses(Long questionId,
                                                             Function<Integer, QuestionVoteDataDto> voteDataResolver) {
        return questionImgRepository.findAllByQuestionId(questionId)
                .stream()
                .map(questionImg -> QuestionImgResDto.of(questionImg, voteDataResolver.apply(questionImg.getSortOrder())))
                .toList();
    }

    public List<QuestionImgSimpleDto> getQuestionImages(Question question) {
        if (!(question instanceof QuestionBuy)) {
            return null;
        }

        return getAllQuestionImages(question);
    }

    public List<QuestionImgSimpleDto> getAllQuestionImages(Question question) {
        return questionImgRepository.findAllByQuestionId(question.getId()).stream()
                .map(QuestionImgSimpleDto::of)
                .toList();
    }

    public List<QuestionImgSimpleDto> getQuestionImagesWithMainImage(Question question) {
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

    public List<QuestionImgSimpleDto> getBuyItemMainImages(Question question) {
        if (!(question instanceof QuestionBuy)) {
            return null;
        }

        return getAllItemMainImages(question);
    }

    public List<QuestionImgSimpleDto> getAllItemMainImages(Question question) {
        return questionItemRepository.findAllByQuestionId(question.getId()).stream()
                .map(questionItem -> {
                    ItemImg mainImg = itemImgRepository.findMainImg(questionItem.getItem().getId());
                    return QuestionImgSimpleDto.of(mainImg);
                })
                .toList();
    }

    public List<QuestionImgSimpleDto> getItemMainImagesForCard(Question question) {
        if (!(question instanceof QuestionBuy)) {
            return new ArrayList<>();
        }

        return getBuyItemMainImages(question);
    }

    public QuestionImgSimpleDto getItemMainImage(Question question) {
        QuestionItem mainQuestionItem = questionItemRepository.findByQuestionIdAndRepresentFlag(question.getId(), true);
        if (mainQuestionItem == null) {
            return null;
        }

        ItemImg mainItemImage = itemImgRepository.findMainImg(mainQuestionItem.getItem().getId());
        return QuestionImgSimpleDto.of(mainItemImage);
    }

    public List<QuestionImgSimpleDto> getQuestionImagesForHome(Question question) {
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
}
