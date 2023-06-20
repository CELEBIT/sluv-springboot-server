package com.sluv.server.domain.search.repository.impl;

import com.querydsl.core.Tuple;

import java.util.List;

public interface SearchDataRepositoryCustom {
    List<Tuple> getTopData();
}
