package com.sluv.domain.item.dto.hashtag;

import com.sluv.domain.item.entity.hashtag.Hashtag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HashtagCountDto {

    private Hashtag hashtag;
    private Long count;

    public static HashtagCountDto of(Hashtag hashtag, Long count) {
        return HashtagCountDto.builder()
                .hashtag(hashtag)
                .count(count)
                .build();
    }

}
