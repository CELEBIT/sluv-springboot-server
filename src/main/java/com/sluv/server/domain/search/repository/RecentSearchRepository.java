package com.sluv.server.domain.search.repository;

import com.sluv.server.domain.search.entity.RecentSearch;
import com.sluv.server.domain.search.repository.impl.RecentSearchRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentSearchRepository extends JpaRepository<RecentSearch, Long>, RecentSearchRepositoryCustom {
}
