package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.TempItem;
import com.sluv.server.domain.item.repository.impl.TempItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TempItemRepository extends JpaRepository<TempItem, Long>, TempItemRepositoryCustom {
    List<TempItem> findAllByUserId(Long id);

    void deleteAllByUserId(Long id);
}
