package com.sluv.server.domain.item.mapper;

import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.repository.ItemImgRepository;
import com.sluv.server.domain.item.repository.ItemScrapRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemDtoMapper {

    private final ItemImgRepository itemImgRepository;
    private final ItemScrapRepository itemScrapRepository;

    public ItemSimpleResDto getItemSimpleResDto(Item item, List<Closet> closetList) {
        return ItemSimpleResDto.of(item, itemImgRepository.findMainImg(item.getId()),
                itemScrapRepository.getItemScrapStatus(item, closetList));
    }
}
