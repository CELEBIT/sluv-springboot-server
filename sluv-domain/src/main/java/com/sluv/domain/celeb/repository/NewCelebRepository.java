package com.sluv.domain.celeb.repository;

import com.sluv.domain.celeb.entity.NewCeleb;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NewCelebRepository extends JpaRepository<NewCeleb, Long> {
    ;

    Optional<NewCeleb> findByCelebName(String newCelebName);
}