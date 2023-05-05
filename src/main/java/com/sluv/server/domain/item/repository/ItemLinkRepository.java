package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.ItemLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemLinkRepository extends JpaRepository<ItemLink, Long> {
    List<ItemLink> findByItemId(Long itemId);
}
