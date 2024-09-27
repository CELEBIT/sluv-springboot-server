package com.sluv.domain.celeb.service;

import com.sluv.domain.celeb.entity.CelebActivity;
import com.sluv.domain.celeb.repository.CelebActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CelebActivityDomainService {

    private final CelebActivityRepository celebActivityRepository;

    public List<CelebActivity> findAllByCelebId(Long celebId) {
        return celebActivityRepository.findAllByCelebId(celebId);
    }

}
