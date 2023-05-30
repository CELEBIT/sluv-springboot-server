package com.sluv.server.domain.closet.service;

import com.sluv.server.domain.closet.dto.ClosetReqDto;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.enums.ClosetStatus;
import com.sluv.server.domain.closet.exception.BasicClosetDeleteException;
import com.sluv.server.domain.closet.exception.ClosetNotFoundException;
import com.sluv.server.domain.closet.repository.ClosetRepository;
import com.sluv.server.domain.item.repository.ItemScrapRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNotMatchedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClosetService {
    private final ClosetRepository closetRepository;
    private final ItemScrapRepository itemScrapRepository;

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

        Closet newCloset = Closet.builder()
                .user(user)
                .name(dto.getName())
                .coverImgUrl(dto.getCoverImgUrl())
                .color(dto.getColor())
                .basicFlag(false)
                .closetStatus(dto.getClosetStatus())
                .build();

        closetRepository.save(newCloset);
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
}
