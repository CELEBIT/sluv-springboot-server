package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.repository.impl.ItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    Long countByUserId(Long user_id);
}