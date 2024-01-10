package com.sluv.server.domain.item.helper;

import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.repository.ClosetRepository;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.repository.ItemImgRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.item.repository.ItemScrapRepository;
import com.sluv.server.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemHelper {

    private final ItemImgRepository itemImgRepository;
    private final ItemScrapRepository itemScrapRepository;
    private final ClosetRepository closetRepository;
    private final ItemRepository itemRepository;

    public ItemSimpleResDto convertItemToSimpleResDto(Item item, User user) {
        List<Closet> closetList = closetRepository.findAllByUserId(user.getId());
//        itemRepository.getItemSimpleResDto(user, )
        return ItemSimpleResDto.of(item, itemImgRepository.findMainImg(item.getId()),
                itemScrapRepository.getItemScrapStatus(item, closetList));
    }
}
