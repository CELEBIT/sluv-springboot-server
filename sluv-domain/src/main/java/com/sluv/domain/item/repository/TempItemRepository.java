package com.sluv.domain.item.repository;

import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.repository.impl.TempItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempItemRepository extends JpaRepository<TempItem, Long>, TempItemRepositoryCustom {
    Long countByUserId(Long id);

    void deleteAllByUserId(Long userId);
}
