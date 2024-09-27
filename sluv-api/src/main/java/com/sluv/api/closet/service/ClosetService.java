package com.sluv.api.closet.service;

import com.sluv.api.closet.dto.request.ClosetRequest;
import com.sluv.api.closet.dto.response.ClosetListCountResponse;
import com.sluv.api.closet.dto.response.ClosetNameCheckResponse;
import com.sluv.api.closet.dto.response.ClosetResponse;
import com.sluv.api.closet.mapper.ClosetMapper;
import com.sluv.domain.closet.dto.ClosetDomainDto;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.closet.exception.BasicClosetDeleteException;
import com.sluv.domain.closet.service.ClosetDomainService;
import com.sluv.domain.item.service.ItemScrapDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.exception.UserNotMatchedException;
import com.sluv.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClosetService {

    private final UserDomainService userDomainService;
    private final ClosetDomainService closetDomainService;
    private final ItemScrapDomainService itemScrapDomainService;

    /**
     * 유저의 옷장 목록 조회
     */
    @Transactional(readOnly = true)
    public ClosetListCountResponse getClosetList(Long userId) {
        User user = userDomainService.findById(userId);
        List<ClosetResponse> closetResponses = closetDomainService.getUserClosetList(user)
                .stream()
                .map(countDto -> ClosetResponse.of(countDto.getCloset(), countDto.getItemCount()))
                .toList();
        Long closetCount = closetDomainService.countByUserId(userId);
        return ClosetListCountResponse.of(closetCount, closetResponses);
    }

    /**
     * 옷장 생성 전 이름 중복 체크
     */
    @Transactional(readOnly = true)
    public ClosetNameCheckResponse checkClosetNameDuplicated(String name, Long closetId) {
        Boolean isDuplicated = closetDomainService.checkDuplicate(name, closetId);
        return ClosetNameCheckResponse.of(isDuplicated);
    }

    /**
     * 신규 유저 생성 시 기본 옷장 생성
     */
    @Transactional
    public void postBasicCloset(User user) {
        closetDomainService.saveBasicCloset(user);
    }

    /**
     * 옷장 생성
     */
    @Transactional
    public void postCloset(Long userId, ClosetRequest dto) {
        User user = userDomainService.findById(userId);
        log.info("옷장 생성 - 사용자: {}, 이름: {}", userId, dto.getName());
        ClosetDomainDto closetDomainDto = ClosetMapper.toClosetDomainDto(dto);
        closetDomainService.saveCloset(Closet.toEntity(user, closetDomainDto));
    }

    /**
     * 옷장 정보 변경
     */
    @Transactional
    public void patchCloset(Long userId, Long closetId, ClosetRequest dto) {
        log.info("옷장 변경 - 사용자: {} ", userId);
        Closet closet = closetDomainService.findById(closetId);
        if (!closet.getUser().getId().equals(userId)) {
            log.info("옷장 변경 실패 - 사용자: {}, 옷장 주인: {}", userId, closet.getUser().getId());
            throw new UserNotMatchedException();
        }

        ClosetDomainDto closetDomainDto = ClosetMapper.toClosetDomainDto(dto);
        closetDomainService.changeClosetData(closet, closetDomainDto);
    }

    /**
     * 옷장 삭제
     */
    @Transactional
    public void deleteCloset(Long userId, Long closetId) {
//        User user = userDomainService.findById(userId);
        Closet closet = closetDomainService.findById(closetId);

        // 현재 유저와 Closet Owner 비교
        if (!closet.getUser().getId().equals(userId)) {
            log.info("User did Not Matched. User Id: {}, Closet Owner Id : {}", userId, closet.getUser().getId());
            throw new UserNotMatchedException();
        }
        // 기본 Closet 여부 확인
        if (closet.getBasicFlag()) {
            log.info("Closet Id {} is Basic Closet", closet.getId());
            throw new BasicClosetDeleteException();
        }

        log.info("ItemScrap Delete By Closet Id: {}", closet.getId());
        itemScrapDomainService.deleteAllByClosetId(closet.getId());

        log.info("Delete Closet Id: {}", closet.getId());
        closetDomainService.deleteById(closet.getId());
    }

}
