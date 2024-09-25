package com.sluv.domain.item.service;

import com.sluv.domain.item.dto.ItemImgDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemImg;
import com.sluv.domain.item.repository.ItemImgRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemImgDomainService {

    private final ItemImgRepository itemImgRepository;

    @Transactional(readOnly = true)
    public ItemImg findMainImg(Long itemId) {
        return itemImgRepository.findMainImg(itemId);
    }

    @Transactional
    public void deleteAllByItemId(Long itemId) {
        itemImgRepository.deleteAllByItemId(itemId);
    }

    @Transactional
    public void saveItemImg(Item item, ItemImgDto dto) {
        ItemImg itemImg = ItemImg.toEntity(item, dto);
        itemImgRepository.save(itemImg);
    }

    @Transactional(readOnly = true)
    public List<ItemImg> findAllByItemId(Long itemId) {
        return itemImgRepository.findAllByItemId(itemId);
    }
}
