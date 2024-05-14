package com.sluv.server.domain.celeb.service;

import com.sluv.server.domain.celeb.dto.CelebActivityResDto;
import com.sluv.server.domain.celeb.repository.CelebActivityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
