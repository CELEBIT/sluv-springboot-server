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
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    @Transactional(readOnly = true)
    public PaginationResDto<HashtagResponseDto> getHashtag(String name, Pageable pageable){
        Page<Tuple> hashtagPage = hashtagRepository.findAllByContent(name, pageable);
        List<HashtagResponseDto> dtoList = hashtagPage
                .stream().map(data ->
                    HashtagResponseDto.of(data.get(0, Hashtag.class), data.get(1, Long.class))
                ).toList();


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
            return  HashtagPostResponseDto.of(hashtag.get());
        }else{ // 없다면 등록후 반환
            Hashtag save = hashtagRepository.save(
                    Hashtag.toEntity(requestDto.getHashtagContent())
            );
            return HashtagPostResponseDto.of(save);
        }

    }

}
