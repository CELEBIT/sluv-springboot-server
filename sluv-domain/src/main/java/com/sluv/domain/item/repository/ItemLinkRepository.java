package com.sluv.domain.item.repository;

import com.sluv.domain.item.entity.ItemLink;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemLinkRepository extends JpaRepository<ItemLink, Long> {
    List<ItemLink> findByItemId(Long itemId);

    void deleteAllByItemId(Long itemId);

    Integer countByItemId(Long itemId);
}
