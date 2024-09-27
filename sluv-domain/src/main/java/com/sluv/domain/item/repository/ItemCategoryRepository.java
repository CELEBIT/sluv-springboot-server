package com.sluv.domain.item.repository;

import com.sluv.domain.item.entity.ItemCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
    List<ItemCategory> findAllByParentNotNull();

    List<ItemCategory> findAllByParentNull();

}
