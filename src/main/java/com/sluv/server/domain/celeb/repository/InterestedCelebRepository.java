package com.sluv.server.domain.celeb.repository;

import com.sluv.server.domain.celeb.entity.InterestedCeleb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestedCelebRepository extends JpaRepository<InterestedCeleb, Long> {
    void deleteAllByUserId(Long userId);
}
