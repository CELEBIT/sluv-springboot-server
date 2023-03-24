package com.sluv.server.domain.item.service;

import com.sluv.server.domain.item.dto.HashtagResponseDto;
import com.sluv.server.domain.item.entity.hashtag.Hashtag;
import com.sluv.server.domain.item.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    public List<HashtagResponseDto> getHashtag(String name, Pageable pageable){
        return hashtagRepository.findAllByContent(name, pageable)
                .stream().map(data -> {
                    Hashtag hashtag = data.get(0, Hashtag.class);

                    return HashtagResponseDto.builder()
                            .hashtagId(hashtag.getId())
                            .hashtagContent(hashtag.getContent())
                            .count(data.get(1, Long.class))
                            .build();
                }).collect(toList());
    }

}
