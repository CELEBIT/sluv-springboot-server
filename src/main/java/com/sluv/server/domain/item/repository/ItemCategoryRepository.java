package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
    List<ItemCategory> findAllByParentNotNull();

    List<ItemCategory> findAllByParentNull();

}
