package com.sluv.server.domain.alarm.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlarmImages {

    private String mainImageUrl;

    private String userImageUrl;

    public static AlarmImages of(String mainImageUrl, String userImageUrl) {
        return AlarmImages.builder()
                .mainImageUrl(mainImageUrl)
                .userImageUrl(userImageUrl)
                .build();
    }

}
