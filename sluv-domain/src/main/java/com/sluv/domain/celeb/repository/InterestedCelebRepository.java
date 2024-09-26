package com.sluv.domain.celeb.repository;

import com.sluv.domain.celeb.entity.InterestedCeleb;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestedCelebRepository extends JpaRepository<InterestedCeleb, Long> {
    void deleteAllByUserId(Long userId);

    List<InterestedCeleb> findAllByUserId(Long user_id);
}
