package com.sluv.server.domain.alarm.dto;

import com.sluv.server.domain.question.dto.QuestionImgSimpleResDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlarmImages {

    private List<QuestionImgSimpleResDto> images;

    private String userImageUrl;

    public static AlarmImages of(List<QuestionImgSimpleResDto> images, String userImageUrl) {
        return AlarmImages.builder()
                .images(images)
                .userImageUrl(userImageUrl)
                .build();
    }

}
