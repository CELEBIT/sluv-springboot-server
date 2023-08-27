package com.sluv.server.domain.closet.service;

import com.sluv.server.domain.closet.dto.ClosetDetailResDto;
import com.sluv.server.domain.closet.dto.ClosetItemSelectReqDto;
import com.sluv.server.domain.closet.dto.ClosetReqDto;
import com.sluv.server.domain.closet.dto.ClosetResDto;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.enums.ClosetStatus;
import com.sluv.server.domain.closet.exception.BasicClosetDeleteException;
import com.sluv.server.domain.closet.exception.ClosetNotFoundException;
import com.sluv.server.domain.closet.repository.ClosetRepository;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.ItemImg;
import com.sluv.server.domain.item.entity.ItemScrap;
import com.sluv.server.domain.item.exception.ItemNotFoundException;
import com.sluv.server.domain.item.repository.ItemImgRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.item.repository.ItemScrapRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNotMatchedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClosetService {
    private final ClosetRepository closetRepository;
    private final ItemScrapRepository itemScrapRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;

    public void postBasicCloset(User user){

        // 기본 Url 지정이 필요
        String DEFAULT_COVER_IMG_URL = "def";

        Closet defCloset = Closet.builder()
                .user(user)
                .name("기본 옷장")
                .coverImgUrl(DEFAULT_COVER_IMG_URL)
                .basicFlag(true)
                .closetStatus(ClosetStatus.PRIVATE)
                .build();

        closetRepository.save(defCloset);
    }

    @Transactional
    public void postCloset(User user, ClosetReqDto dto) {

        closetRepository.save(
                Closet.toEntity(user, dto)
        );
    }

    @Transactional
    public void patchCloset(User user, Long closetId, ClosetReqDto dto) {
        Closet closet = closetRepository.findById(closetId).orElseThrow(ClosetNotFoundException::new);

        if(!closet.getUser().getId().equals(user.getId())){
            log.info( "User Id: {}, Closet Owner Id : {}",user.getId(), closet.getUser().getId());
            throw new UserNotMatchedException();
        }

        closet.changeClosetCover(dto.getName(), dto.getCoverImgUrl(), dto.getColor(), dto.getClosetStatus());

    }

    @Transactional
    public void deleteCloset(User user, Long closetId) {
        Closet closet = closetRepository.findById(closetId).orElseThrow(ClosetNotFoundException::new);

        // 현재 유저와 Closet Owner 비교
        if(!closet.getUser().getId().equals(user.getId())){
            log.info( "User did Not Matched. User Id: {}, Closet Owner Id : {}",user.getId(), closet.getUser().getId());
            throw new UserNotMatchedException();
        }
        // 기본 Closet 여부 확인
        if(closet.getBasicFlag()){
            log.info("Closet Id {} is Basic Closet", closet.getId());
            throw new BasicClosetDeleteException();
        }

        log.info("ItemScrap Delete By Closet Id: {}", closet.getId());
        itemScrapRepository.deleteAllByClosetId(closet.getId());

        log.info("Delete Closet Id: {}", closet.getId());
        closetRepository.deleteById(closet.getId());
    }

    @Transactional
    public void postItemScrapToCloset(User user, Long itemId, Long closetId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        Closet closet = closetRepository.findById(closetId).orElseThrow(ClosetNotFoundException::new);

        if(!closet.getUser().getId().equals(user.getId())){
            log.info( "User did Not Matched. User Id: {}, Closet Owner Id : {}",user.getId(), closet.getUser().getId());
            throw new UserNotMatchedException();
        }

        log.info("Save ItemScrap with item Id: {}, Closet Id {}", item.getId(), closet.getId());
        ItemScrap saveItemScrap = itemScrapRepository.save(
                ItemScrap.toEntity(item, closet)
        );
        log.info("Save Success with ItemScrap Id: {}", saveItemScrap.getId());

        item.decreaseViewNum();

    }

    @Transactional
    public void patchItems(User user, Long closetId, ClosetItemSelectReqDto dto) {
        Closet closet = closetRepository.findById(closetId).orElseThrow(ClosetNotFoundException::new);

        if(!closet.getUser().getId().equals(user.getId())){
            log.info( "User did Not Matched. User Id: {}, Closet Owner Id : {}",user.getId(), closet.getUser().getId());
            throw new UserNotMatchedException();
        }

        dto.getItemList().forEach(itemId -> {
            log.info("Delete Item {}, from Closet {}", itemId, closet.getId());
            itemScrapRepository.deleteByClosetIdAndItemId(closet.getId(), itemId);
        });

    }

    @Transactional
    public void deleteItemScrapFromCloset(User user, Long itemId) {
        List<Closet> closetList = closetRepository.findAllByUserId(user.getId());

        log.info("DELETE ItemScrap {}", itemId);
        closetList.forEach(closet -> {
            itemScrapRepository.deleteByClosetIdAndItemId(closet.getId(), itemId);
        });

    }

    @Transactional
    public void patchSaveCloset(User user, Long fromClosetId, Long toClosetId, ClosetItemSelectReqDto dto) {
        Closet fromCloset = closetRepository.findById(fromClosetId).orElseThrow(ClosetNotFoundException::new);
        Closet toCloset = closetRepository.findById(toClosetId).orElseThrow(ClosetNotFoundException::new);

        // fromCloset과 현재 유저 일치 비교
        if(!fromCloset.getUser().getId().equals(user.getId())){
            log.info( "User did Not Matched. User Id: {}, fromCloset Owner Id : {}",user.getId(), fromCloset.getUser().getId());
            throw new UserNotMatchedException();
        }

        // toCloset과 현재 유저 일치 비교
        if(!toCloset.getUser().getId().equals(user.getId())){
            log.info( "User did Not Matched. User Id: {}, toCloset Owner Id : {}",user.getId(), toCloset.getUser().getId());
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

    public ClosetDetailResDto<ItemSimpleResDto> getClosetDetails(User user, Long closetId, Pageable pageable) {
        Closet closet = closetRepository.findById(closetId).orElseThrow(ClosetNotFoundException::new);

        if(!closet.getUser().getId().equals(user.getId())){
            log.info( "User did Not Matched. User Id: {}, Closet Owner Id : {}", user.getId(), closet.getUser().getId());
            throw new UserNotMatchedException();
        }

        Page<Item> itemPage = itemRepository.getClosetItems(closet, pageable);

        List<ItemSimpleResDto> content = getItemContent(user, itemPage);

        return ClosetDetailResDto.<ItemSimpleResDto>builder()
                .hasNext(itemPage.hasNext())
                .page(itemPage.getNumber())
                .content(content)
                .coverImgUrl(closet.getCoverImgUrl())
                .name(closet.getName())
                .closetStatus(closet.getClosetStatus())
                .itemNum(itemPage.getTotalElements())
                .build();


//        return new ClosetDetailResDto<>(itemPage.hasNext(), itemPage.getNumber(), content, closet.getCoverImgUrl(), closet.getName(), closet.getClosetStatus(), itemPage.getTotalElements());

    }

    private List<ItemSimpleResDto> getItemContent(User user, Page<Item> itemPage) {
        List<Closet> closetList = closetRepository.findAllByUserId(user.getId());

        return itemPage.stream()
                .map(item -> {
                    ItemImg mainImg = itemImgRepository.findMainImg(item.getId());
                    Boolean itemScrapStatus = itemScrapRepository.getItemScrapStatus(item, closetList);
                    return ItemSimpleResDto.of(item,mainImg, itemScrapStatus);
                }).toList();
    }

    public List<ClosetResDto> getClosetList(User user) {
        List<Closet> closetList = closetRepository.findAllByUserId(user.getId());


        return closetList
                .stream().map(closet -> ClosetResDto.of(
                                                    closet,
                                                    itemScrapRepository.countByClosetId(closet.getId())
                                                    )
                ).toList();
    }
}
