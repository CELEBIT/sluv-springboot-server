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
@RequiredArgsConstructor
public class PlaceRankService {
    private final PlaceRankRepository placeRankRepository;

    public List<PlaceRankResDto> getTopPlace(){
        return placeRankRepository.findTopPlace().stream().map(placeName -> PlaceRankResDto.builder()
                                                                                        .placeName(placeName)
                                                                                        .build()
                                                                ).toList();
    }

    public void postPlace(User user, PlaceRankReqDto dto) {
        placeRankRepository.save(
                PlaceRank.builder()
                        .user(user)
                        .place(dto.getPlaceName())
                        .build()
        );

    }

    @Transactional
    public void deleteAllPlace(User user) {
        placeRankRepository.deleteAllByUserId(user.getId());
    }
}
