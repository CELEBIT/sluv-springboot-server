package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.ItemScrap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemScrapRepository extends JpaRepository<ItemScrap, Long> {
    void deleteAllByClosetId(Long id);
}
