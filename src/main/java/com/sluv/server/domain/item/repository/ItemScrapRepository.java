package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.ItemScrap;
import com.sluv.server.domain.item.repository.impl.ItemScrapRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemScrapRepository extends JpaRepository<ItemScrap, Long>, ItemScrapRepositoryCustom {
    void deleteAllByClosetId(Long id);

    void deleteByClosetIdAndItemId(Long closet_id, Long item_id);

    ItemScrap findByClosetIdAndItemId(Long fromClosetId, Long itemId);

    Boolean existsByClosetIdAndItemId(Long closet_id, Long item_id);

    Long countByClosetId(Long id);

    Integer countByItemId(Long id);
}
