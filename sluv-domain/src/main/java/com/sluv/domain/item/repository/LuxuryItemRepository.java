package com.sluv.domain.item.repository;

import com.sluv.domain.item.entity.LuxuryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LuxuryItemRepository extends JpaRepository<LuxuryItem, Long> {
}
