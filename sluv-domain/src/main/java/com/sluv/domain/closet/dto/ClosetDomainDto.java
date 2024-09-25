package com.sluv.domain.closet.dto;

import com.sluv.domain.closet.enums.ClosetColor;
import com.sluv.domain.closet.enums.ClosetStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClosetDomainDto {
    private String name;
    private String coverImgUrl;
    private ClosetStatus closetStatus;
    private ClosetColor colorScheme;

    public static ClosetDomainDto of(String name, String coverImgUrl, ClosetStatus closetStatus,
                                     ClosetColor colorScheme) {
        return ClosetDomainDto.builder()
                .name(name)
                .coverImgUrl(coverImgUrl)
                .closetStatus(closetStatus)
                .colorScheme(colorScheme)
                .build();
    }

}
