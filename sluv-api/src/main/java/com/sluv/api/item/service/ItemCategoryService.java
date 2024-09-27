package com.sluv.api.item.service;

import com.sluv.api.item.dto.ItemCategoryChildResponseDto;
import com.sluv.api.item.dto.ItemCategoryParentResponseDto;
import com.sluv.domain.item.entity.ItemCategory;
import com.sluv.domain.item.service.ItemCategoryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemCategoryService {

    private final ItemCategoryDomainService itemCategoryDomainService;

    @Transactional(readOnly = true)
    public List<ItemCategoryParentResponseDto> getItemCategory() {

        List<ItemCategory> childList = itemCategoryDomainService.findAllByParentNotNull();

        return itemCategoryDomainService.findAllByParentNull()
                .stream().map(parent -> {
                    List<ItemCategoryChildResponseDto> childDtoList = childList.stream()
                            .filter(child -> Objects.equals(child.getParent().getId(), parent.getId()))
                            .map(ItemCategoryChildResponseDto::of).toList();

                    return ItemCategoryParentResponseDto.of(parent, childDtoList);

                }).toList();
    }

}
