package com.sluv.api.celeb.service;

import com.sluv.api.celeb.dto.response.CelebActivityResponse;
import com.sluv.domain.celeb.service.CelebActivityDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CelebActivityService {

    private final CelebActivityDomainService celebActivityDomainService;

    public List<CelebActivityResponse> getCelebActivity(Long celebId) {
        return celebActivityDomainService.findAllByCelebId(celebId).stream()
                .map(CelebActivityResponse::of)
                .toList();
    }

}
