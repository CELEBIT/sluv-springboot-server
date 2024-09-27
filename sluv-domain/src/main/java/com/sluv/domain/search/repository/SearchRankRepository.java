package com.sluv.domain.search.repository;

import com.sluv.domain.search.entity.SearchRank;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRankRepository extends JpaRepository<SearchRank, Long> {
    List<SearchRank> findAllByOrderBySearchCountDesc();

}
