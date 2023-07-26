package com.sluv.server.domain.closet.repository;

import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.repository.impl.ClosetRepositoryCustom;
import com.sluv.server.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClosetRepository extends JpaRepository<Closet, Long>, ClosetRepositoryCustom {
    List<Closet> findAllByUserId(Long id);
}
