package com.sluv.api.item.service;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.item.dto.HashtagPostResponseDto;
import com.sluv.api.item.dto.HashtagRequestDto;
import com.sluv.api.item.dto.HashtagResponseDto;
import com.sluv.api.item.helper.HashtagHelper;
import com.sluv.domain.item.dto.hashtag.HashtagCountDto;
import com.sluv.domain.item.entity.hashtag.Hashtag;
import com.sluv.domain.item.service.hashtag.HashtagDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagDomainService hashtagDomainService;
    private final HashtagHelper hashtagHelper;

    @Transactional(readOnly = true)
    public PaginationResponse<HashtagResponseDto> getHashtag(String name, Pageable pageable) {
        Page<HashtagCountDto> hashtagPage = hashtagDomainService.findAllByContent(name, pageable);
        List<HashtagResponseDto> dtoList = hashtagPage
                .stream().map(hashtagHelper::convertHashtagCountDtoToHashtagResponseDto).toList();
        return PaginationResponse.create(hashtagPage, dtoList);
    }

    @Transactional
    public HashtagPostResponseDto postHashtag(HashtagRequestDto requestDto) {

        // 해당 Hashtag가 있는지 검색
        Optional<Hashtag> hashtag = hashtagDomainService.findByContent(requestDto.getHashtagContent());

        //있다면 해당 Hashtag를 반환
        if (hashtag.isPresent()) {
            return HashtagPostResponseDto.of(hashtag.get());
        } else { // 없다면 등록 후 반환
            Hashtag save = hashtagDomainService.saveHashtag(requestDto.getHashtagContent());
            return HashtagPostResponseDto.of(save);
        }

    }

}
