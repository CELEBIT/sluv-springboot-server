package com.sluv.domain.item.service;

import com.sluv.domain.item.entity.ItemCategory;
import com.sluv.domain.item.exception.ItemCategoryNotFoundException;
import com.sluv.domain.item.repository.ItemCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemCategoryDomainService {

    private final ItemCategoryRepository itemCategoryRepository;

    public ItemCategory findById(Long categoryId) {
        return itemCategoryRepository.findById(categoryId).orElseThrow(ItemCategoryNotFoundException::new);
    }

    public List<ItemCategory> findAllByParentNotNull() {
        return itemCategoryRepository.findAllByParentNotNull();
    }

    public List<ItemCategory> findAllByParentNull() {
        return itemCategoryRepository.findAllByParentNull();
    }

}
