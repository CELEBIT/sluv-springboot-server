package com.sluv.server.domain.closet.repository;

import com.sluv.server.domain.closet.entity.Closet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClosetRepository extends JpaRepository<Closet, Long> {
}
