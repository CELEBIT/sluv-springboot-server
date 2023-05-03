package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.PlaceRank;
import com.sluv.server.domain.item.repository.impl.PlaceRankRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRankRepository extends JpaRepository<PlaceRank, Long>, PlaceRankRepositoryCustom {
    void deleteAllByUserId(Long id);
}
