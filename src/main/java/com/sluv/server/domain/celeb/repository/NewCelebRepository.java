package com.sluv.server.domain.celeb.repository;

import com.sluv.server.domain.celeb.entity.NewCeleb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface NewCelebRepository extends JpaRepository<NewCeleb, Long>{ ;
    Optional<NewCeleb> findByCelebName(String newCelebName);
}