package com.sluv.api.item.helper;

import com.sluv.api.item.dto.HashtagResponseDto;
import com.sluv.domain.item.dto.hashtag.HashtagCountDto;
import org.springframework.stereotype.Component;

@Component
public class HashtagHelper {

    public HashtagResponseDto convertHashtagCountDtoToHashtagResponseDto(HashtagCountDto dto) {
        return HashtagResponseDto.of(dto.getHashtag(), dto.getCount());
    }

}
