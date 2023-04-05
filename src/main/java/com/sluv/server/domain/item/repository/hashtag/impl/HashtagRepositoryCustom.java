package com.sluv.server.domain.item.repository.hashtag.impl;

import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HashtagRepositoryCustom {
    Page<Tuple> findAllByContent(String name, Pageable pageable);
}
