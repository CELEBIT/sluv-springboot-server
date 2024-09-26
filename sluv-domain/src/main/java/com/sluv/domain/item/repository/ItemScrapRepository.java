package com.sluv.domain.item.repository;

import com.sluv.domain.item.entity.ItemScrap;
import com.sluv.domain.item.repository.impl.ItemScrapRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemScrapRepository extends JpaRepository<ItemScrap, Long>, ItemScrapRepositoryCustom {
    void deleteAllByClosetId(Long id);

    void deleteByClosetIdAndItemId(Long closet_id, Long item_id);

    ItemScrap findByClosetIdAndItemId(Long fromClosetId, Long itemId);

    Boolean existsByClosetIdAndItemId(Long closet_id, Long item_id);

    Long countByClosetId(Long id);

    Integer countByItemId(Long id);
}
