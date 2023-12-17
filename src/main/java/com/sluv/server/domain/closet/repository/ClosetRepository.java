package com.sluv.server.domain.closet.repository;

import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.repository.impl.ClosetRepositoryCustom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClosetRepository extends JpaRepository<Closet, Long>, ClosetRepositoryCustom {
    List<Closet> findAllByUserId(Long id);

    Long countByUserId(Long id);
}
