package com.sluv.domain.celeb.repository;

import com.sluv.domain.celeb.entity.NewCeleb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface NewCelebRepository extends JpaRepository<NewCeleb, Long> {

    Optional<NewCeleb> findByCelebName(String newCelebName);

    List<NewCeleb> findAllByOrderByCreatedAtDesc();
}