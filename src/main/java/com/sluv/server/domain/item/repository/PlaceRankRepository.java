package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.PlaceRank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRankRepository extends JpaRepository<PlaceRank, Long> {
    void deleteAllByUserId(Long id);

    void deleteByUserIdAndPlace(Long id, String placeName);
}
