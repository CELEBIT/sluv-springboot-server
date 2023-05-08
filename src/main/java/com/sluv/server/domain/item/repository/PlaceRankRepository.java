package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.PlaceRank;
import com.sluv.server.domain.item.repository.impl.PlaceRankRepositoryCustom;
import com.sluv.server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;

public interface PlaceRankRepository extends JpaRepository<PlaceRank, Long>, PlaceRankRepositoryCustom {
    void deleteAllByUserId(Long id);

    void deleteByUserIdAndPlace(Long id, String placeName);
}
