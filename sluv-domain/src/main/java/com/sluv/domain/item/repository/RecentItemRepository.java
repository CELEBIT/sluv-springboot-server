package com.sluv.domain.item.repository;

import com.sluv.domain.item.entity.RecentItem;
import com.sluv.domain.item.repository.impl.RecentItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentItemRepository extends JpaRepository<RecentItem, Long>, RecentItemRepositoryCustom {
    void deleteAllByUserId(Long userId);
}
