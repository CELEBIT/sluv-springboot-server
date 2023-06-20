package com.sluv.server.domain.search.repository;

import com.sluv.server.domain.search.entity.SearchData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchDataRepository extends JpaRepository<SearchData, Long> {
}
