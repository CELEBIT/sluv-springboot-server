package com.sluv.server.domain.closet.repository;

import com.sluv.server.domain.closet.entity.Closet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClosetRepository extends JpaRepository<Closet, Long> {
    List<Closet> findAllByUserId(Long id);
}
