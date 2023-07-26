package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.DayHotItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayHotItemRepository extends JpaRepository<DayHotItem, Long> {
}
