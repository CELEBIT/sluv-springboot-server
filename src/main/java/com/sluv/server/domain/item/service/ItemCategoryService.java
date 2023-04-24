package com.sluv.server.domain.item.service;

import com.sluv.server.domain.item.dto.ItemCategoryChildResponseDto;
import com.sluv.server.domain.item.dto.ItemCategoryParentResponseDto;
import com.sluv.server.domain.item.entity.ItemCategory;
import com.sluv.server.domain.item.repository.ItemCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemCategoryService {

    private final ItemCategoryRepository itemCategoryRepository;
    public List<ItemCategoryParentResponseDto> getItemCategory(){


        List<ItemCategory> childList = itemCategoryRepository.findAllByParentNotNull();


        return itemCategoryRepository.findAllByParentNull()
                .stream().map(parent -> {
                    List<ItemCategoryChildResponseDto> childDtoList = childList.stream()
                            .filter(child -> Objects.equals(child.getParent().getId(), parent.getId()))
                            .map(child -> ItemCategoryChildResponseDto.builder()
                                    .id(child.getId())
                                    .name(child.getName())
                                    .build()
                            ).toList();

                   return ItemCategoryParentResponseDto.builder()
                            .id(parent.getId())
                            .name(parent.getName())
                            .subCategoryList(childDtoList)
                            .build();

                }).toList();
    }
}
