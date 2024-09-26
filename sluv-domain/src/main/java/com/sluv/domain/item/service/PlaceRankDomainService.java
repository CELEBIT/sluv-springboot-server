package com.sluv.domain.item.service;

import com.sluv.domain.item.entity.PlaceRank;
import com.sluv.domain.item.repository.PlaceRankRepository;
import com.sluv.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceRankDomainService {

    private final PlaceRankRepository placeRankRepository;

    @Transactional
    public void savePlaceRank(User user, String placeName) {
        PlaceRank placeRank = PlaceRank.toEntity(user, placeName);
        placeRankRepository.save(placeRank);
    }

    @Transactional
    public void deleteAllByUserId(Long userId) {
        placeRankRepository.deleteAllByUserId(userId);
    }

    @Transactional
    public void deleteByUserIdAndPlace(Long userId, String placeName) {
        placeRankRepository.deleteByUserIdAndPlace(userId, placeName);
    }

    @Transactional(readOnly = true)
    public List<PlaceRank> getRecentPlaceTop20(Long userId) {
        return placeRankRepository.getRecentPlaceTop20(userId);
    }

}
