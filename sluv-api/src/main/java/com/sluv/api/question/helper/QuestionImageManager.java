package com.sluv.api.question.helper;

import com.sluv.api.question.dto.QuestionImgReqDto;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.QuestionImg;
import com.sluv.domain.question.service.QuestionImgDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionImageManager {

    private final QuestionImgDomainService questionImgDomainService;

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
}
