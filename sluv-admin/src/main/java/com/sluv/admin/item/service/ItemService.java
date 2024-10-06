package com.sluv.admin.item.service;

import com.sluv.admin.item.dto.HotItemResDto;
import com.sluv.admin.user.dto.UserCountByCategoryResDto;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.CelebCategory;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.service.ItemDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemDomainService itemDomainService;

    public UserCountByCategoryResDto getItemCountByCelebCategoryParent() {
        List<Item> allItem = itemDomainService.getAllItemWithCelebCategory();

        HashMap<String, Long> collect = allItem.stream().map(Item::getCeleb)
                .map(Celeb::getCelebCategory)
                .map(celebCategory -> celebCategory.getParent() == null ? celebCategory : celebCategory.getParent())
                .collect(Collectors.groupingBy(CelebCategory::getName, HashMap::new, Collectors.counting()));

        return UserCountByCategoryResDto.of(collect, allItem.stream().count());
    }

    public UserCountByCategoryResDto getItemCountByCelebCategoryChild(String parentCategory) {
        List<Item> allItem = itemDomainService.getAllItemWithCelebCategory();

        HashMap<String, Long> collect = allItem.stream().map(Item::getCeleb)
                .map(Celeb::getCelebCategory)
                .filter(celebCategory -> celebCategory.getParent() != null)
                .filter(celebCategory -> celebCategory.getParent().getName().equals(parentCategory))
                .collect(Collectors.groupingBy(CelebCategory::getName, HashMap::new, Collectors.counting()));

        return UserCountByCategoryResDto.of(collect, allItem.stream().count());
    }

    public List<HotItemResDto> getTop3HotItem() {
        return itemDomainService.getTop3HotItem().stream()
                .map(HotItemResDto::from)
                .toList();
    }

}
