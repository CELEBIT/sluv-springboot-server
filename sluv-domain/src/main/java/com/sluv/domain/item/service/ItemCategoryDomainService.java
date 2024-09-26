package com.sluv.domain.item.service;

import com.sluv.domain.item.entity.ItemCategory;
import com.sluv.domain.item.exception.ItemCategoryNotFoundException;
import com.sluv.domain.item.repository.ItemCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemCategoryDomainService {

    private final ItemCategoryRepository itemCategoryRepository;

    @Transactional(readOnly = true)
    public ItemCategory findById(Long categoryId) {
        return itemCategoryRepository.findById(categoryId).orElseThrow(ItemCategoryNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<ItemCategory> findAllByParentNotNull() {
        return itemCategoryRepository.findAllByParentNotNull();
    }

    @Transactional(readOnly = true)
    public List<ItemCategory> findAllByParentNull() {
        return itemCategoryRepository.findAllByParentNull();
    }

}
