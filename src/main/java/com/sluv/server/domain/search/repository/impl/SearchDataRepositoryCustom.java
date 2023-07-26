package com.sluv.server.domain.search.repository.impl;

import com.querydsl.core.Tuple;
import com.sluv.server.domain.search.entity.SearchData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchDataRepositoryCustom {
    List<Tuple> getTopData();

    Page<SearchData> getSearchKeyword(String keyword, Pageable pageable);
}
