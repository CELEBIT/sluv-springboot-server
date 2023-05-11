package com.sluv.server.domain.item.service;

import com.querydsl.core.Tuple;
import com.sluv.server.domain.item.dto.HashtagPostResponseDto;
import com.sluv.server.domain.item.dto.HashtagRequestDto;
import com.sluv.server.domain.item.dto.HashtagResponseDto;
import com.sluv.server.domain.item.entity.hashtag.Hashtag;
import com.sluv.server.domain.item.repository.hashtag.HashtagRepository;
import com.sluv.server.global.common.response.PaginationResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    public PaginationResDto<HashtagResponseDto> getHashtag(String name, Pageable pageable){
        Page<Tuple> hashtagPage = hashtagRepository.findAllByContent(name, pageable);
        List<HashtagResponseDto> dtoList = hashtagPage
                .stream().map(data -> {
                    Hashtag hashtag = data.get(0, Hashtag.class);

                    return HashtagResponseDto.builder()
                            .hashtagId(hashtag.getId())
                            .hashtagContent(hashtag.getContent())
                            .count(data.get(1, Long.class))
                            .build();
                }).toList();


        return PaginationResDto.<HashtagResponseDto>builder()
                .hasNext(hashtagPage.hasNext())
                .page(hashtagPage.getNumber())
                .content(dtoList)
                .build();
    }

    public HashtagPostResponseDto postHashtag(HashtagRequestDto requestDto){

        // 해당 Hashtag가 있는지 검색
        Optional<Hashtag> hashtag = hashtagRepository.findByContent(requestDto.getHashtagContent());

        //있다면 해당 Hashtag를 반환
        if(hashtag.isPresent()){
            return  HashtagPostResponseDto.builder()
                    .hashtagId(hashtag.get().getId())
                    .hashtagContent(hashtag.get().getContent())
                    .build();
        }else{ // 없다면 등록후 반환
            Hashtag save = hashtagRepository.save(Hashtag.builder()
                    .content(requestDto.getHashtagContent())
                    .build()
            );
            return HashtagPostResponseDto.builder()
                    .hashtagId(save.getId())
                    .hashtagContent(save.getContent())
                    .build();
        }

    }

}
