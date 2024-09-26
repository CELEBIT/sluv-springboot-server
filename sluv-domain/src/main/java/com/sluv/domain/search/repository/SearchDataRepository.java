package com.sluv.domain.search.repository;

import com.sluv.domain.search.entity.SearchData;
import com.sluv.domain.search.repository.impl.SearchDataRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchDataRepository extends JpaRepository<SearchData, Long>, SearchDataRepositoryCustom {
}
