package com.sluv.domain.search.repository;

import com.sluv.domain.search.entity.RecentSearch;
import com.sluv.domain.search.repository.impl.RecentSearchRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentSearchRepository extends JpaRepository<RecentSearch, Long>, RecentSearchRepositoryCustom {
    void deleteByUserIdAndSearchWord(Long id, String keyword);

    void deleteAllByUserId(Long id);
}
