package com.sluv.server.domain.item.helper;

import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.repository.ClosetRepository;
import com.sluv.server.domain.comment.dto.CommentItemResDto;
import com.sluv.server.domain.comment.entity.CommentItem;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.ItemImg;
import com.sluv.server.domain.item.repository.ItemImgRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.item.repository.ItemScrapRepository;
import com.sluv.server.domain.user.entity.User;
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

    public ItemSimpleResDto convertItemToSimpleResDto(Item item, User user) {
        List<Closet> closetList = closetRepository.findAllByUserId(user.getId());
//        itemRepository.getItemSimpleResDto(user, )
        return ItemSimpleResDto.of(item, itemImgRepository.findMainImg(item.getId()),
                itemScrapRepository.getItemScrapStatus(item, closetList));
    }

    /**
     * CommentItem -> CommentItemResDto 변경하는 메소드
     *
     * @param commentItem
     * @return CommentItemResDto
     */
    public CommentItemResDto getCommentItemResDto(CommentItem commentItem, User user) {
        ItemImg mainImg = itemImgRepository.findMainImg(commentItem.getItem().getId());
        List<Closet> closetList = new ArrayList<>();
        if (user != null) {
            closetList = closetRepository.findAllByUserId(user.getId());
        }
        Boolean itemScrapStatus = itemScrapRepository.getItemScrapStatus(commentItem.getItem(), closetList);

        return CommentItemResDto.of(commentItem, mainImg, itemScrapStatus);
    }
}
