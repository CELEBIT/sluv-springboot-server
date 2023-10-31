package com.sluv.server.domain.celeb.service;

import com.sluv.server.domain.celeb.dto.CelebActivityResDto;
import com.sluv.server.domain.celeb.repository.CelebActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CelebActivityService {
    private final CelebActivityRepository celebActivityRepository;

    @Transactional(readOnly = true)
    public List<CelebActivityResDto> getCelebActivity(Long celebId) {

        return celebActivityRepository.findAllByCelebId(celebId).stream()
                .map(CelebActivityResDto::of)
                .toList();
    }
}
