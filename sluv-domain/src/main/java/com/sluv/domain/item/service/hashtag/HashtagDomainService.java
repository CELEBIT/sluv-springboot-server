package com.sluv.domain.item.service.hashtag;

import com.sluv.domain.item.dto.hashtag.HashtagCountDto;
import com.sluv.domain.item.entity.hashtag.Hashtag;
import com.sluv.domain.item.exception.hashtag.HashtagNotFoundException;
import com.sluv.domain.item.repository.hashtag.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HashtagDomainService {

    private final HashtagRepository hashtagRepository;

    public Hashtag findById(Long hashTagId) {
        return hashtagRepository.findById(hashTagId).orElseThrow(HashtagNotFoundException::new);
    }

    public Page<HashtagCountDto> findAllByContent(String name, Pageable pageable) {
        return hashtagRepository.findAllByContent(name, pageable);
    }

    public Optional<Hashtag> findByContent(String hashtagContent) {
        return hashtagRepository.findByContent(hashtagContent);
    }

    public Hashtag saveHashtag(String hashtagContent) {
        Hashtag hashtag = Hashtag.toEntity(hashtagContent);
        return hashtagRepository.save(hashtag);
    }
}
