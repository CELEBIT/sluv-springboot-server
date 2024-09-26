package com.sluv.domain.item.repository;

import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.enums.ItemStatus;
import com.sluv.domain.item.repository.impl.ItemRepositoryCustom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    Long countByUserIdAndItemStatus(Long user_id, ItemStatus itemStatus);

    List<Item> findAllByUserId(Long userId);

    List<Item> findAllByItemStatus(ItemStatus itemStatus);
}