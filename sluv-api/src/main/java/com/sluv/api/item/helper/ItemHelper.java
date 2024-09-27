package com.sluv.api.item.helper;

import com.sluv.api.comment.dto.reponse.CommentItemResponse;
import com.sluv.api.item.dto.ItemPostReqDto;
import com.sluv.api.item.dto.TempItemPostReqDto;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.closet.repository.ClosetRepository;
import com.sluv.domain.comment.entity.CommentItem;
import com.sluv.domain.item.dto.ItemSaveDto;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.item.dto.TempItemSaveDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemImg;
import com.sluv.domain.item.repository.ItemImgRepository;
import com.sluv.domain.item.repository.ItemRepository;
import com.sluv.domain.item.repository.ItemScrapRepository;
import com.sluv.domain.user.entity.User;
import java.util.ArrayList;
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

    public ItemSimpleDto convertItemToSimpleResDto(Item item, User user) {
        List<Closet> closetList = closetRepository.findAllByUserId(user.getId());
//        itemRepository.getItemSimpleResDto(user, )
        return ItemSimpleDto.of(item, itemImgRepository.findMainImg(item.getId()),
                itemScrapRepository.getItemScrapStatus(item, closetList));
    }

    /**
     * CommentItem -> CommentItemResDto 변경하는 메소드
     *
     * @param commentItem
     * @return CommentItemResDto
     */
    public CommentItemResponse getCommentItemResDto(CommentItem commentItem, User user) {
        ItemImg mainImg = itemImgRepository.findMainImg(commentItem.getItem().getId());
        List<Closet> closetList = new ArrayList<>();
        if (user != null) {
            closetList = closetRepository.findAllByUserId(user.getId());
        }
        Boolean itemScrapStatus = itemScrapRepository.getItemScrapStatus(commentItem.getItem(), closetList);

        return CommentItemResponse.of(commentItem, mainImg, itemScrapStatus);
    }

    public ItemSaveDto convertItemPostReqDtoToItemSaveDto(ItemPostReqDto dto) {
        return ItemSaveDto.builder()
                .itemName(dto.getItemName())
                .additionalInfo(dto.getAdditionalInfo())
                .infoSource(dto.getInfoSource())
                .price(dto.getPrice())
                .whenDiscovery(dto.getWhenDiscovery())
                .whereDiscovery(dto.getWhereDiscovery())
                .build();
    }

    public TempItemSaveDto convertTempItemPostReqDtoToTempItemSaveDto(TempItemPostReqDto dto) {
        return TempItemSaveDto.builder()
                .itemName(dto.getItemName())
                .additionalInfo(dto.getAdditionalInfo())
                .infoSource(dto.getInfoSource())
                .price(dto.getPrice())
                .whenDiscovery(dto.getWhenDiscovery())
                .whereDiscovery(dto.getWhereDiscovery())
                .build();
    }

}
