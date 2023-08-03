package com.sluv.server.domain.celeb.repository;

import com.sluv.server.domain.celeb.entity.InterestedCeleb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestedCelebRepository extends JpaRepository<InterestedCeleb, Long> {
    void deleteAllByUserId(Long userId);

    List<InterestedCeleb> findAllByUserId(Long user_id);
}
