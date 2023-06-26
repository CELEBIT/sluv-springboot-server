package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.WeekHotItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeekHotItemRepository extends JpaRepository<WeekHotItem, Long> {
}
