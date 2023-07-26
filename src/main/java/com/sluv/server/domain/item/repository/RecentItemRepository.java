package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.RecentItem;
import com.sluv.server.domain.item.repository.impl.RecentItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentItemRepository extends JpaRepository<RecentItem, Long>, RecentItemRepositoryCustom {
}
