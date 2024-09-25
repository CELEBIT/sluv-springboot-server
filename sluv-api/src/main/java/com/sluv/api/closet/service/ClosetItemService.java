package com.sluv.api.closet.service;

import com.sluv.api.closet.dto.request.ClosetItemSelectRequest;
import com.sluv.api.closet.dto.response.ClosetDetailResponse;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.closet.enums.ClosetStatus;
import com.sluv.domain.closet.service.ClosetDomainService;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemScrap;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.item.service.ItemScrapDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.exception.UserNotMatchedException;
import com.sluv.domain.user.service.UserDomainService;
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

    private final UserDomainService userDomainService;
    private final ItemDomainService itemDomainService;
    private final ItemScrapDomainService itemScrapDomainService;
    private final ClosetDomainService closetDomainService;

    /**
     * 옷장 상세 조회
     */
    @Transactional(readOnly = true)
    public ClosetDetailResponse<ItemSimpleDto> getClosetDetails(Long userId, Long closetId, Pageable pageable) {
        Closet closet = closetDomainService.findById(closetId);
        if (closet.getClosetStatus().equals(ClosetStatus.PRIVATE) && !closet.getUser().getId().equals(userId)) {
            log.info("User did Not Matched. User Id: {}, Closet Owner Id : {}", userId, closet.getUser().getId());
            throw new UserNotMatchedException();
        }
        Page<ItemSimpleDto> itemPage = itemDomainService.getClosetItems(closet, pageable);
        return ClosetDetailResponse.of(itemPage, closet);
    }

    /**
     * 아이템 스크랩
     */
    @Transactional
    public void postItemScrapToCloset(Long userId, Long itemId, Long closetId) {
        Item item = itemDomainService.findById(itemId);
        Closet closet = closetDomainService.findById(closetId);

        if (!closet.getUser().getId().equals(userId)) {
            log.info("User did Not Matched. User Id: {}, Closet Owner Id : {}", userId, closet.getUser().getId());
            throw new UserNotMatchedException();
        }

        log.info("Save ItemScrap with item Id: {}, Closet Id {}", item.getId(), closet.getId());
        ItemScrap saveItemScrap = itemScrapDomainService.saveItemScrap(item, closet);
        log.info("Save Success with ItemScrap Id: {}", saveItemScrap.getId());


    }

    /**
     * 아이템 스크랩 취소
     */
    @Transactional
    public void deleteItemScrapFromCloset(Long userId, Long itemId) {
        User user = userDomainService.findById(userId);
        List<Closet> closetList = closetDomainService.findAllByUserId(user.getId());

        log.info("DELETE ItemScrap {}", itemId);
        closetList.forEach(closet -> {
            itemScrapDomainService.deleteByClosetIdAndItemId(closet.getId(), itemId);
        });
    }

    /**
     * 옷장에서 선택한 아이템 삭제
     */
    @Transactional
    public void removeSelectItemInCloset(Long userId, Long closetId, ClosetItemSelectRequest dto) {
        Closet closet = closetDomainService.findById(closetId);

        if (!closet.getUser().getId().equals(userId)) {
            log.info("User did Not Matched. User Id: {}, Closet Owner Id : {}", userId, closet.getUser().getId());
            throw new UserNotMatchedException();
        }

        dto.getItemList().forEach(itemId -> {
            log.info("Delete Item {}, from Closet {}", itemId, closet.getId());
            itemScrapDomainService.deleteByClosetIdAndItemId(closet.getId(), itemId);
        });

    }


    /**
     * 특정 옷장에 있는 아이템을 다른 옷장으로 이동
     */
    @Transactional
    public void moveItemInCloset(Long userId, Long fromClosetId, Long toClosetId, ClosetItemSelectRequest dto) {
        Closet fromCloset = closetDomainService.findById(fromClosetId);
        Closet toCloset = closetDomainService.findById(toClosetId);

        // fromCloset과 현재 유저 일치 비교
        if (!fromCloset.getUser().getId().equals(userId)) {
            log.info("User did Not Matched. User Id: {}, fromCloset Owner Id : {}", userId,
                    fromCloset.getUser().getId());
            throw new UserNotMatchedException();
        }

        // toCloset과 현재 유저 일치 비교
        if (!toCloset.getUser().getId().equals(userId)) {
            log.info("User did Not Matched. User Id: {}, toCloset Owner Id : {}", userId, toCloset.getUser().getId());
            throw new UserNotMatchedException();
        }

        // Target ItemScrap 모두 조회
        List<ItemScrap> itemScrapList = dto.getItemList()
                .stream()
                .map(itemId -> itemScrapDomainService.findByClosetIdAndItemId(fromClosetId, itemId)).toList();

        // Target ItemScrap의 Closet을 toCloset으로 모두 변경
        itemScrapList.forEach(itemScrap -> itemScrap.changeCloset(toCloset));

    }
}
