package com.sluv.server.domain.closet.service;

import com.sluv.server.domain.closet.dto.ClosetDetailResDto;
import com.sluv.server.domain.closet.dto.ClosetItemSelectReqDto;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.enums.ClosetStatus;
import com.sluv.server.domain.closet.exception.ClosetNotFoundException;
import com.sluv.server.domain.closet.repository.ClosetRepository;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.ItemScrap;
import com.sluv.server.domain.item.exception.ItemNotFoundException;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.item.repository.ItemScrapRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNotMatchedException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ClosetItemService {
    private final ItemRepository itemRepository;
    private final ItemScrapRepository itemScrapRepository;
    private final ClosetRepository closetRepository;

    /**
     * 옷장 상세 조회
     */
    @Transactional(readOnly = true)
    public ClosetDetailResDto<ItemSimpleResDto> getClosetDetails(User user, Long closetId, Pageable pageable) {
        Closet closet = closetRepository.findById(closetId).orElseThrow(ClosetNotFoundException::new);
        if (closet.getClosetStatus().equals(ClosetStatus.PRIVATE) && !closet.getUser().getId().equals(user.getId())) {
            log.info("User did Not Matched. User Id: {}, Closet Owner Id : {}", user.getId(),
                    closet.getUser().getId());
            throw new UserNotMatchedException();
        }
        Page<ItemSimpleResDto> itemPage = itemRepository.getClosetItems(closet, pageable);
        return ClosetDetailResDto.of(itemPage, closet);
    }

    /**
     * 아이템 스크랩
     */
    @Transactional
    public void postItemScrapToCloset(User user, Long itemId, Long closetId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        Closet closet = closetRepository.findById(closetId).orElseThrow(ClosetNotFoundException::new);

        if (!closet.getUser().getId().equals(user.getId())) {
            log.info("User did Not Matched. User Id: {}, Closet Owner Id : {}", user.getId(), closet.getUser().getId());
            throw new UserNotMatchedException();
        }

        log.info("Save ItemScrap with item Id: {}, Closet Id {}", item.getId(), closet.getId());
        ItemScrap saveItemScrap = itemScrapRepository.save(
                ItemScrap.toEntity(item, closet)
        );
        log.info("Save Success with ItemScrap Id: {}", saveItemScrap.getId());


    }

    /**
     * 아이템 스크랩 취소
     */
    @Transactional
    public void deleteItemScrapFromCloset(User user, Long itemId) {
        List<Closet> closetList = closetRepository.findAllByUserId(user.getId());

        log.info("DELETE ItemScrap {}", itemId);
        closetList.forEach(closet -> {
            itemScrapRepository.deleteByClosetIdAndItemId(closet.getId(), itemId);
        });
    }

    /**
     * 옷장에서 선택한 아이템 삭제
     */
    @Transactional
    public void removeSelectItemInCloset(User user, Long closetId, ClosetItemSelectReqDto dto) {
        Closet closet = closetRepository.findById(closetId).orElseThrow(ClosetNotFoundException::new);

        if (!closet.getUser().getId().equals(user.getId())) {
            log.info("User did Not Matched. User Id: {}, Closet Owner Id : {}", user.getId(), closet.getUser().getId());
            throw new UserNotMatchedException();
        }

        dto.getItemList().forEach(itemId -> {
            log.info("Delete Item {}, from Closet {}", itemId, closet.getId());
            itemScrapRepository.deleteByClosetIdAndItemId(closet.getId(), itemId);
        });

    }


    /**
     * 특정 옷장에 있는 아이템을 다른 옷장으로 이동
     */
    @Transactional
    public void moveItemInCloset(User user, Long fromClosetId, Long toClosetId, ClosetItemSelectReqDto dto) {
        Closet fromCloset = closetRepository.findById(fromClosetId).orElseThrow(ClosetNotFoundException::new);
        Closet toCloset = closetRepository.findById(toClosetId).orElseThrow(ClosetNotFoundException::new);

        // fromCloset과 현재 유저 일치 비교
        if (!fromCloset.getUser().getId().equals(user.getId())) {
            log.info("User did Not Matched. User Id: {}, fromCloset Owner Id : {}", user.getId(),
                    fromCloset.getUser().getId());
            throw new UserNotMatchedException();
        }

        // toCloset과 현재 유저 일치 비교
        if (!toCloset.getUser().getId().equals(user.getId())) {
            log.info("User did Not Matched. User Id: {}, toCloset Owner Id : {}", user.getId(),
                    toCloset.getUser().getId());
            throw new UserNotMatchedException();
        }

        // Target ItemScrap 모두 조회
        List<ItemScrap> itemScrapList = dto.getItemList()
                .stream()
                .map(itemId ->
                        itemScrapRepository.findByClosetIdAndItemId(fromClosetId, itemId)
                ).toList();

        // Target ItemScrap의 Closet을 toCloset으로 모두 변경
        itemScrapList.forEach(itemScrap ->
                itemScrap.changeCloset(toCloset)
        );

    }
}
