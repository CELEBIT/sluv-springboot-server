package com.sluv.domain.item.repository.hashtag.impl;

import com.sluv.domain.item.dto.hashtag.HashtagCountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HashtagRepositoryCustom {
    Page<HashtagCountDto> findAllByContent(String name, Pageable pageable);
}
