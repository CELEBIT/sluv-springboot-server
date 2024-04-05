package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.ItemLike;
import com.sluv.server.domain.item.repository.impl.ItemLikeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemLikeRepository extends JpaRepository<ItemLike, Long>, ItemLikeRepositoryCustom {
    boolean existsByUserIdAndItemId(Long id, Long itemId);

    void deleteByUserIdAndItemId(Long id, Long itemId);

    Integer countByItemId(Long itemId);
}
