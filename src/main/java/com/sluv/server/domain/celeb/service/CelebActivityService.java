package com.sluv.server.domain.celeb.service;

import com.sluv.server.domain.celeb.dto.CelebActivityResDto;
import com.sluv.server.domain.celeb.repository.CelebActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CelebActivityService {
    private final CelebActivityRepository celebActivityRepository;


    public List<CelebActivityResDto> getCelebActivity(Long celebId) {

        return celebActivityRepository.findAllByCelebId(celebId).stream()
                .map(celebActivity -> CelebActivityResDto.builder()
                        .activityName(celebActivity.getActivityName())
                        .build()
                ).toList();
    }
}
