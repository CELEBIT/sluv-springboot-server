package com.sluv.api.item.service;

import com.sluv.api.item.dto.PlaceRankReqDto;
import com.sluv.api.item.dto.PlaceRankResDto;
import com.sluv.domain.item.service.PlaceRankDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceRankService {

    private final PlaceRankDomainService placeRankDomainService;
    private final UserDomainService userDomainService;

    public void postPlace(Long userId, PlaceRankReqDto dto) {
        User user = userDomainService.findById(userId);
        placeRankDomainService.savePlaceRank(user, dto.getPlaceName());

    }

    public void deleteAllPlace(Long userId) {
        placeRankDomainService.deleteAllByUserId(userId);
    }

    public void deletePlace(Long userId, String placeName) {
        placeRankDomainService.deleteByUserIdAndPlace(userId, placeName);
    }

    @Transactional(readOnly = true)
    public List<PlaceRankResDto> getRecentPlaceTop20(Long userId) {
        return placeRankDomainService.getRecentPlaceTop20(userId)
                .stream()
                .map(PlaceRankResDto::of)
                .toList();
    }
}
