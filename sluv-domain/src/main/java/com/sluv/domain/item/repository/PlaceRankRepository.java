package com.sluv.domain.item.repository;

import com.sluv.domain.item.entity.PlaceRank;
import com.sluv.domain.item.repository.impl.PlaceRankRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRankRepository extends JpaRepository<PlaceRank, Long>, PlaceRankRepositoryCustom {
    void deleteAllByUserId(Long id);

    void deleteByUserIdAndPlace(Long id, String placeName);
}
