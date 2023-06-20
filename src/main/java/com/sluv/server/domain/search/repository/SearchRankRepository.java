package com.sluv.server.domain.search.repository;

import com.sluv.server.domain.search.entity.SearchRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRankRepository extends JpaRepository<SearchRank, Long> {
    List<SearchRank> findAllByOrderBySearchCountDesc();

}
