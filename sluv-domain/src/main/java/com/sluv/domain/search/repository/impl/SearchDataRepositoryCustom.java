package com.sluv.domain.search.repository.impl;

import com.querydsl.core.Tuple;
import com.sluv.domain.search.entity.SearchData;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchDataRepositoryCustom {
    List<Tuple> getTopData();

    Page<SearchData> getSearchKeyword(String keyword, Pageable pageable);
}
