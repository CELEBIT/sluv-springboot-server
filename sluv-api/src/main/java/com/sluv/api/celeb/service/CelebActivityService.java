package com.sluv.api.celeb.service;

import com.sluv.api.celeb.dto.response.CelebActivityResponse;
import com.sluv.domain.celeb.service.CelebActivityDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CelebActivityService {

    private final CelebActivityDomainService celebActivityDomainService;

    @Transactional(readOnly = true)
    public List<CelebActivityResponse> getCelebActivity(Long celebId) {
        return celebActivityDomainService.findAllByCelebId(celebId).stream()
                .map(CelebActivityResponse::of)
                .toList();
    }

}
