package com.sluv.server.domain.item.service;

import com.sluv.server.domain.item.dto.PlaceRankReqDto;
import com.sluv.server.domain.item.dto.PlaceRankResDto;
import com.sluv.server.domain.item.entity.PlaceRank;
import com.sluv.server.domain.item.repository.PlaceRankRepository;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceRankService {
    private final PlaceRankRepository placeRankRepository;

    public void postPlace(User user, PlaceRankReqDto dto) {
        placeRankRepository.save(
                PlaceRank.toEntity(user, dto)
        );

    }

    public void deleteAllPlace(User user) {
        placeRankRepository.deleteAllByUserId(user.getId());
    }

    public void deletePlace(User user, String placeName) {
        placeRankRepository.deleteByUserIdAndPlace(user.getId(), placeName);
    }

    @Transactional(readOnly = true)
    public List<PlaceRankResDto> getRecentPlaceTop20(User user) {
        return placeRankRepository.getRecentPlaceTop20(user)
                            .stream()
                            .map(PlaceRankResDto::of).toList();
    }
}
