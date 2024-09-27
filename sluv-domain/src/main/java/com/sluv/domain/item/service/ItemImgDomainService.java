package com.sluv.domain.item.service;

import com.sluv.domain.item.dto.ItemImgDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemImg;
import com.sluv.domain.item.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemImgDomainService {

    private final ItemImgRepository itemImgRepository;

    public ItemImg findMainImg(Long itemId) {
        return itemImgRepository.findMainImg(itemId);
    }

    public void deleteAllByItemId(Long itemId) {
        itemImgRepository.deleteAllByItemId(itemId);
    }

    public void saveItemImg(Item item, ItemImgDto dto) {
        ItemImg itemImg = ItemImg.toEntity(item, dto);
        itemImgRepository.save(itemImg);
    }

    public List<ItemImg> findAllByItemId(Long itemId) {
        return itemImgRepository.findAllByItemId(itemId);
    }
}
