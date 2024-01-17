package com.sluv.server.domain.closet.service;

import com.sluv.server.domain.closet.dto.ClosetListCountResDto;
import com.sluv.server.domain.closet.dto.ClosetNameCheckResDto;
import com.sluv.server.domain.closet.dto.ClosetReqDto;
import com.sluv.server.domain.closet.dto.ClosetResDto;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.enums.ClosetColor;
import com.sluv.server.domain.closet.enums.ClosetStatus;
import com.sluv.server.domain.closet.exception.BasicClosetDeleteException;
import com.sluv.server.domain.closet.exception.ClosetNotFoundException;
import com.sluv.server.domain.closet.repository.ClosetRepository;
import com.sluv.server.domain.item.repository.ItemScrapRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNotMatchedException;
import java.util.List;
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

    /**
     * 유저의 옷장 목록 조회
     */
    @Transactional(readOnly = true)
    public ClosetListCountResDto getClosetList(User user) {
        List<ClosetResDto> closetResDtos = closetRepository.getUserClosetList(user);
        return ClosetListCountResDto.of(closetRepository.countByUserId(user.getId()), closetResDtos);
    }

    /**
     * 옷장 생성 전 이름 중복 체크
     */
    @Transactional(readOnly = true)
    public ClosetNameCheckResDto checkClosetNameDuplicated(String name, Long closetId) {
        Boolean isDuplicated = closetRepository.checkDuplicate(name, closetId);

        return ClosetNameCheckResDto.of(isDuplicated);
    }

    /**
     * 신규 유저 생성 시 기본 옷장 생성
     */
    @Transactional
    public void postBasicCloset(User user) {
        Closet basicCloset = Closet.builder()
                .user(user)
                .name("기본 옷장")
                .coverImgUrl(null)
                .basicFlag(true)
                .color(ClosetColor.PURPLE)
                .closetStatus(ClosetStatus.PRIVATE)
                .build();

        closetRepository.save(basicCloset);
    }

    /**
     * 옷장 생성
     */
    @Transactional
    public void postCloset(User user, ClosetReqDto dto) {
        log.info("옷장 생성 - 사용자: {}, 이름: {}", user.getId(), dto.getName());
        closetRepository.save(
                Closet.toEntity(user, dto)
        );
    }

    /**
     * 옷장 정보 변경
     */
    @Transactional
    public void patchCloset(User user, Long closetId, ClosetReqDto dto) {
        Closet closet = closetRepository.findById(closetId).orElseThrow(ClosetNotFoundException::new);
        log.info("옷장 변경 - 사용자: {} ", user.getId());
        if (!closet.getUser().getId().equals(user.getId())) {
            log.info("옷장 변경 실패 - 사용자: {}, 옷장 주인: {}", user.getId(), closet.getUser().getId());
            throw new UserNotMatchedException();
        }
        closet.changeCloset(dto);
    }

    /**
     * 옷장 삭제
     */
    @Transactional
    public void deleteCloset(User user, Long closetId) {
        Closet closet = closetRepository.findById(closetId).orElseThrow(ClosetNotFoundException::new);

        // 현재 유저와 Closet Owner 비교
        if (!closet.getUser().getId().equals(user.getId())) {
            log.info("User did Not Matched. User Id: {}, Closet Owner Id : {}", user.getId(), closet.getUser().getId());
            throw new UserNotMatchedException();
        }
        // 기본 Closet 여부 확인
        if (closet.getBasicFlag()) {
            log.info("Closet Id {} is Basic Closet", closet.getId());
            throw new BasicClosetDeleteException();
        }

        log.info("ItemScrap Delete By Closet Id: {}", closet.getId());
        itemScrapRepository.deleteAllByClosetId(closet.getId());

        log.info("Delete Closet Id: {}", closet.getId());
        closetRepository.deleteById(closet.getId());
    }

}
