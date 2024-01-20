package com.sluv.server.global.cache;

import org.springframework.stereotype.Service;

@Service
public interface CacheService {
    void insert(Long memberId);

    Long getCount();

    void clear();
}
