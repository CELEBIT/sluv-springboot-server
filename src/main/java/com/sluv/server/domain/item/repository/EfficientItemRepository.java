package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.EfficientItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EfficientItemRepository extends JpaRepository<EfficientItem, Long> {
}
