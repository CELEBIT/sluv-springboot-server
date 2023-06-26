package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.LuxuryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LuxuryItemRepository extends JpaRepository<LuxuryItem, Long> {
}
