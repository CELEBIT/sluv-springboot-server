package com.sluv.domain.item.service;

import com.sluv.domain.item.entity.PlaceRank;
import com.sluv.domain.item.repository.PlaceRankRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceRankDomainService {

    private final PlaceRankRepository placeRankRepository;

    public void savePlaceRank(User user, String placeName) {
        PlaceRank placeRank = PlaceRank.toEntity(user, placeName);
        placeRankRepository.save(placeRank);
    }

    public void deleteAllByUserId(Long userId) {
        placeRankRepository.deleteAllByUserId(userId);
    }

    public void deleteByUserIdAndPlace(Long userId, String placeName) {
        placeRankRepository.deleteByUserIdAndPlace(userId, placeName);
    }

    public List<PlaceRank> getRecentPlaceTop20(Long userId) {
        return placeRankRepository.getRecentPlaceTop20(userId);
    }

}
