package com.sluv.server.domain.item.service;

import com.sluv.server.domain.item.dto.PlaceRankResDto;
import com.sluv.server.domain.item.repository.PlaceRankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
