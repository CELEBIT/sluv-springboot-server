package com.sluv.domain.item.repository;

import com.sluv.domain.item.entity.ItemLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemLikeRepository extends JpaRepository<ItemLike, Long> {
    boolean existsByUserIdAndItemId(Long id, Long itemId);

    void deleteByUserIdAndItemId(Long id, Long itemId);

    Integer countByItemId(Long itemId);
}
