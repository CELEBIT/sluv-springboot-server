package com.sluv.server.domain.celeb.repository;

import com.sluv.server.domain.celeb.entity.NewCeleb;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NewCelebRepository extends JpaRepository<NewCeleb, Long>{
}