package com.sluv.domain.closet.repository;

import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.closet.repository.impl.ClosetRepositoryCustom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClosetRepository extends JpaRepository<Closet, Long>, ClosetRepositoryCustom {
    List<Closet> findAllByUserId(Long id);

    Long countByUserId(Long id);
}
