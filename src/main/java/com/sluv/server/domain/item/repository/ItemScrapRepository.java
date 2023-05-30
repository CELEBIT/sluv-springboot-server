package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.ItemScrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemScrapRepository extends JpaRepository<ItemScrap, Long> {
    void deleteAllByClosetId(Long id);

    void deleteByClosetIdAndItemId(Long closet_id, Long item_id);
}
